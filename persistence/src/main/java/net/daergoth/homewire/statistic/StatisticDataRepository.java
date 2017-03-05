package net.daergoth.homewire.statistic;

import com.mongodb.Block;
import com.mongodb.client.MongoDatabase;
import net.daergoth.homewire.CustomMongoRepository;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Repository
public class StatisticDataRepository extends CustomMongoRepository {

  private final Logger logger = LoggerFactory.getLogger(StatisticDataRepository.class);

  private static final String COLLECTION_NAME = "statistic_data";

  private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

  @Autowired
  public StatisticDataRepository(MongoDatabase db) {
    super(db);
  }

  @Override
  protected String getCollectionName() {
    return COLLECTION_NAME;
  }

  public void saveSensorData(SensorMeasurementEntity sensorMeasurementEntity) {
    logger.info("Saving statistic data: {}", sensorMeasurementEntity);

    if (sensorMeasurementEntity.getValue() == null) {
      logger.warn("Sensor measurement with null value!");

      return;
    }

    LocalDateTime hour =
        sensorMeasurementEntity.getTime().toLocalDateTime().truncatedTo(ChronoUnit.HOURS);

    Document query = new Document()
        .append("dev_id", sensorMeasurementEntity.getId())
        .append("date_hour", Date.from(hour.toInstant(ZoneOffset.UTC)))
        .append("type", sensorMeasurementEntity.getType());

    if (collection.count(query) > 0) {
      Document arrayQuery = new Document(query)
          .append("values.minute", sensorMeasurementEntity.getTime().getMinute());

      if (collection.count(arrayQuery) > 0) {
        Document updated = new Document()
            .append("$inc", new Document()
                .append("values.$.num", 1)
                .append("values.$.sum", sensorMeasurementEntity.getValue())
            );

        collection.updateOne(arrayQuery, updated);

      } else {
        Document arrayElement = new Document()
            .append("$push", new Document()
                .append("values", new Document()
                    .append("minute", sensorMeasurementEntity.getTime().getMinute())
                    .append("num", 1)
                    .append("sum", sensorMeasurementEntity.getValue())
                )
            );

        collection.updateOne(query, arrayElement);
      }

    } else {
      Document newDocument = new Document()
          .append("dev_id", sensorMeasurementEntity.getId())
          .append("date_hour", Date.from(hour.toInstant(ZoneOffset.UTC)))
          .append("type", sensorMeasurementEntity.getType())
          .append("values",
              Collections.singletonList(new Document()
                  .append("minute", sensorMeasurementEntity.getTime().getMinute())
                  .append("num", 1)
                  .append("sum", sensorMeasurementEntity.getValue())
              )
          );

      collection.insertOne(newDocument);
    }
  }

  public List<SensorMeasurementEntity> getSensorDataWithInterval(
      SensorMeasurementEntity.MeasurementInterval measurementInterval) {
    return getSensorDataWithIntervalWithType("", measurementInterval);
  }

  public List<SensorMeasurementEntity> getSensorDataWithIntervalWithType(
      String sensorType,
      SensorMeasurementEntity.MeasurementInterval measurementInterval) {

    List<SensorMeasurementEntity> result = new ArrayList<>();

    List<Document> aggregateDocument = new ArrayList<>();

    if (!sensorType.isEmpty()) {
      aggregateDocument.add(
          new Document()
              .append("$match", new Document("type", sensorType))
      );
    }

    switch (measurementInterval) {
      case CURRENT:
      case MINUTE:
        aggregateDocument = Arrays.asList(
            new Document()
                .append("$unwind", "$values"),
            new Document()
                .append("$project", new Document()
                    .append("_id", 0)
                    .append("dev_id", 1)
                    .append("type", 1)
                    .append("date", new Document()
                        .append("$add", Arrays.asList(
                            "$date_hour",
                            new Document()
                                .append("$multiply", Arrays.asList(
                                    "$values.minute",
                                    60000
                                ))
                        ))
                    )
                    .append("ave", new Document()
                        .append("$divide", Arrays.asList(
                            "$values.sum",
                            "$values.num"
                        ))
                    )
                ),
            new Document()
                .append("$sort", new Document("date", 1))
        );
        break;
      case HOUR:
        aggregateDocument = Arrays.asList(
            new Document()
                .append("$project", new Document()
                    .append("_id", 0)
                    .append("dev_id", 1)
                    .append("type", 1)
                    .append("date", "$date_hour")
                    .append("ave", new Document()
                        .append("$avg", new Document()
                            .append("$map", new Document()
                                .append("input", "$values")
                                .append("as", "value")
                                .append("in", new Document()
                                    .append("$divide", Arrays.asList(
                                        "$$value.sum",
                                        "$$value.num"
                                    ))
                                )
                            )
                        )
                    )
                ),
            new Document()
                .append("$sort", new Document("date", 1))
        );
        break;
      case DAY:
        aggregateDocument = Arrays.asList(
            new Document()
                .append("$project", new Document()
                    .append("_id", 0)
                    .append("dev_id", 1)
                    .append("type", 1)
                    .append("date", new Document()
                        .append("$subtract", Arrays.asList(
                            "$date_hour",
                            new Document()
                                .append("$multiply", Arrays.asList(
                                    new Document("$hour", "$date_hour"),
                                    60,
                                    60000
                                ))
                        ))
                    )
                    .append("ave", new Document()
                        .append("$divide", Arrays.asList(
                            new Document("$sum", "$values.sum"),
                            new Document("$sum", "$values.num")
                        ))
                    )
                ),
            new Document()
                .append("$group", new Document()
                    .append("_id", new Document()
                        .append("date", "$date")
                        .append("dev_id", "$dev_id")
                        .append("type", "$type")
                    )
                    .append("ave", new Document("$avg", "$ave"))
                ),
            new Document()
                .append("$project", new Document()
                    .append("_id", 0)
                    .append("dev_id", "$_id.dev_id")
                    .append("type", "$_id.type")
                    .append("date", "$_id.date")
                    .append("ave", "$ave")
                ),
            new Document()
                .append("$sort", new Document("date", 1))
        );
        break;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

    collection.aggregate(aggregateDocument)
        .forEach((Block<? super Document>) document -> result.add(new SensorMeasurementEntity(
            document.getInteger("dev_id").shortValue(),
            document.getString("type"),
            document.getDouble("ave").floatValue(),
            ZonedDateTime
                .parse(dateFormat.format(document.getDate("date")), dateTimeFormatter),
            measurementInterval)
        ));

    return result;
  }

}
