package net.daergoth.homewire.setup;

import com.mongodb.Block;
import com.mongodb.client.MongoDatabase;
import net.daergoth.homewire.CustomMongoRepository;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SensorSetupRepository extends CustomMongoRepository {

  private final Logger logger = LoggerFactory.getLogger(SensorSetupRepository.class);

  private static final String COLLECTION_NAME = "sensor_setup";

  @Autowired
  public SensorSetupRepository(MongoDatabase db) {
    super(db);
  }

  @Override
  protected String getCollectionName() {
    return COLLECTION_NAME;
  }

  public void assignNameToSensor(Short devId, String type, String name) {
    logger.info("Assigned name '{}' to ({}, {})", name, devId, type);

    Document query = new Document("dev_id", devId)
        .append("type", type);
    Document updated = new Document("name", name);

    collection.updateOne(query, updated);
  }

  public void setTrustedStatusToSensor(Short devId, String type, boolean isTrusted) {
    logger.info("Trusted status set to '{}' for ({}, {})", isTrusted, devId, type);

    Document query = new Document("dev_id", devId)
        .append("type", type);
    Document updated = new Document("isTrusted", isTrusted);

    collection.updateOne(query, updated);
  }

  public void saveSensorEntity(SensorEntity sensorEntity) {
    logger.info("New sensor added: {}", sensorEntity);

    Document sensorDoc = new Document()
        .append("dev_id", sensorEntity.getDevId())
        .append("name", sensorEntity.getName())
        .append("type", sensorEntity.getType())
        .append("isTrusted", sensorEntity.isTrusted());

    collection.insertOne(sensorDoc);
  }

  public List<String> getSensorTypes() {
    List<String> result = new ArrayList<>();

    collection.distinct("type", String.class)
        .forEach((Block<? super String>) result::add);

    return result;
  }

  public List<SensorEntity> getAllSensorEntities() {
    List<SensorEntity> result = new ArrayList<>();

    collection.find().forEach((Block<? super Document>) document -> {
      Short devId = document.getInteger("dev_id").shortValue();
      String name = document.getString("name");
      String type = document.getString("type");
      boolean isTrusted = document.getBoolean("isTrusted");

      result.add(new SensorEntity(devId, name, type, isTrusted));
    });

    return result;
  }

  public SensorEntity getSensorEntityByIdAndType(Short devId, String type) {
    Document query = new Document("dev_id", devId)
        .append("type", type);

    Document sensorDoc = collection.find(query).first();

    if (sensorDoc == null) {
      return null;
    }

    String name = sensorDoc.getString("name");
    boolean isTrusted = sensorDoc.getBoolean("isTrusted");

    return new SensorEntity(devId, name, type, isTrusted);
  }

  public List<SensorEntity> getSensorEntityByName(String name) {
    Document query = new Document("name", name);

    List<SensorEntity> result = new ArrayList<>();

    collection.find(query).forEach((Block<? super Document>) document -> {
      Short devId = document.getInteger("dev_id").shortValue();
      String type = document.getString("type");
      boolean isTrusted = document.getBoolean("isTrusted");

      result.add(new SensorEntity(devId, name, type, isTrusted));
    });

    return result;
  }

  public List<SensorEntity> getSensorEntitiesByType(String type) {
    Document query = new Document("type", type);

    List<SensorEntity> result = new ArrayList<>();

    collection.find(query).forEach((Block<? super Document>) document -> {
      Short devId = document.getInteger("dev_id").shortValue();
      String name = document.getString("name");
      boolean isTrusted = document.getBoolean("isTrusted");

      result.add(new SensorEntity(devId, name, type, isTrusted));
    });

    return result;
  }

  public void updateSensorEntity(SensorEntity sensorEntity) {
    Document query = new Document("dev_id", sensorEntity.getDevId())
        .append("type", sensorEntity.getType());

    Document update = new Document()
        .append("$set", new Document()
            .append("name", sensorEntity.getName())
            .append("isTrusted", sensorEntity.isTrusted())
        );

    collection.updateOne(query, update);
  }
}
