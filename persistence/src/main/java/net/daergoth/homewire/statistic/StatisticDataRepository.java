package net.daergoth.homewire.statistic;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import net.daergoth.homewire.CustomMongoRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Repository
public class StatisticDataRepository extends CustomMongoRepository {

  private static final String COLLECTION_NAME = "statistic_data";

  @Autowired
  public StatisticDataRepository(MongoDatabase db) {
    super(db);
  }

  @Override
  protected String getCollectionName() {
    return COLLECTION_NAME;
  }

  public void saveSensorData(SensorMeasurementEntity sensorMeasurementEntity) {
    ZonedDateTime hour = sensorMeasurementEntity.getTime().truncatedTo(ChronoUnit.HOURS);

    Document query = new Document()
        .append("dev_id", sensorMeasurementEntity.getId())
        .append("date_hour", Date.from(hour.toInstant()))
        .append("type", sensorMeasurementEntity.getType());

    if (collection.count(query) > 0) {
      BasicDBObject updated = new BasicDBObject()
          .append("$inc",
              new BasicDBObject()
                  .append(
                      "values." + String.valueOf(sensorMeasurementEntity.getTime().getMinute())
                          + ".num",
                      1)
                  .append(
                      "values." + String.valueOf(sensorMeasurementEntity.getTime().getMinute())
                          + ".sum",
                      sensorMeasurementEntity.getValue())
          );

      collection.updateOne(query, updated);
    } else {
      Document newDocument = new Document()
          .append("dev_id", sensorMeasurementEntity.getId())
          .append("date_hour", Date.from(hour.toInstant()))
          .append("type", sensorMeasurementEntity.getType())
          .append("values",
              new BasicDBObject(String.valueOf(sensorMeasurementEntity.getTime().getMinute()),
                  new BasicDBObject("num", 1)
                      .append("sum", sensorMeasurementEntity.getValue())
              )
          );
      collection.insertOne(newDocument);
    }
  }

}
