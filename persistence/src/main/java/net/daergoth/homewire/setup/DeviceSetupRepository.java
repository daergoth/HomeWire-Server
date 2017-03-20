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
public class DeviceSetupRepository extends CustomMongoRepository {

  private final Logger logger = LoggerFactory.getLogger(DeviceSetupRepository.class);

  private static final String COLLECTION_NAME = "device_setup";

  @Autowired
  public DeviceSetupRepository(MongoDatabase db) {
    super(db);
  }

  @Override
  protected String getCollectionName() {
    return COLLECTION_NAME;
  }

  public void assignNameToDevice(Short devId, String type, String name) {
    logger.info("Assigned name '{}' to ({}, {})", name, devId, type);

    Document query = new Document("dev_id", devId)
        .append("type", type);
    Document updated = new Document("name", name);

    collection.updateOne(query, updated);
  }

  public void setTrustedStatusToDevice(Short devId, String type, boolean isTrusted) {
    logger.info("Trusted status set to '{}' for ({}, {})", isTrusted, devId, type);

    Document query = new Document("dev_id", devId)
        .append("type", type);
    Document updated = new Document("isTrusted", isTrusted);

    collection.updateOne(query, updated);
  }

  public void saveDeviceEntity(DeviceEntity deviceEntity) {
    logger.info("New device added: {}", deviceEntity);

    Document sensorDoc = new Document()
        .append("dev_id", deviceEntity.getDevId())
        .append("name", deviceEntity.getName())
        .append("category", deviceEntity.getCategory())
        .append("type", deviceEntity.getType())
        .append("isTrusted", deviceEntity.isTrusted());

    collection.insertOne(sensorDoc);
  }

  public List<String> getSensorTypes() {
    List<String> result = new ArrayList<>();

    collection.distinct("type", new Document("category", "sensor"), String.class)
        .forEach((Block<? super String>) result::add);

    return result;
  }

  public List<String> getActorTypes() {
    List<String> result = new ArrayList<>();

    collection.distinct("type", new Document("category", "actor"), String.class)
        .forEach((Block<? super String>) result::add);

    return result;
  }

  public List<DeviceEntity> getAllDevicesByCategory(String category) {
    List<DeviceEntity> result = new ArrayList<>();

    Document filter;
    if (category.isEmpty()) {
      filter = new Document();
    } else {
      filter = new Document("category", category);
    }

    collection.find(filter)
        .forEach((Block<? super Document>) document -> {
          Short devId = document.getInteger("dev_id").shortValue();
          String name = document.getString("name");
          String cat = document.getString("category");
          String type = document.getString("type");
          boolean isTrusted = document.getBoolean("isTrusted");

          result.add(new DeviceEntity(devId, name, cat, type, isTrusted));
        });

    return result;
  }

  public List<DeviceEntity> getAllDeviceEntities() {
    return getAllDevicesByCategory("");
  }

  public List<DeviceEntity> getAllSensorEntities() {
    return getAllDevicesByCategory("sensor");
  }

  public List<DeviceEntity> getAllActorEntities() {
    return getAllDevicesByCategory("actor");
  }

  public DeviceEntity getDeviceEntityByIdAndType(Short devId, String type) {
    Document query = new Document("dev_id", devId)
        .append("type", type);

    Document deviceDoc = collection.find(query).first();

    if (deviceDoc == null) {
      return null;
    }

    String name = deviceDoc.getString("name");
    String category = deviceDoc.getString("category");
    boolean isTrusted = deviceDoc.getBoolean("isTrusted");

    return new DeviceEntity(devId, name, category, type, isTrusted);
  }

  public List<DeviceEntity> getDeviceEntitiesByName(String name) {
    Document query = new Document("name", name);

    List<DeviceEntity> result = new ArrayList<>();

    collection.find(query).forEach((Block<? super Document>) document -> {
      Short devId = document.getInteger("dev_id").shortValue();
      String category = document.getString("category");
      String type = document.getString("type");
      boolean isTrusted = document.getBoolean("isTrusted");

      result.add(new DeviceEntity(devId, name, category, type, isTrusted));
    });

    return result;
  }

  public List<DeviceEntity> getDeviceEntitiesByType(String type) {
    Document query = new Document("type", type);

    List<DeviceEntity> result = new ArrayList<>();

    collection.find(query).forEach((Block<? super Document>) document -> {
      Short devId = document.getInteger("dev_id").shortValue();
      String category = document.getString("category");
      String name = document.getString("name");
      boolean isTrusted = document.getBoolean("isTrusted");

      result.add(new DeviceEntity(devId, name, category, type, isTrusted));
    });

    return result;
  }

  public void updateDeviceEntity(DeviceEntity deviceEntity) {
    Document query = new Document("dev_id", deviceEntity.getDevId())
        .append("type", deviceEntity.getType());

    Document update = new Document()
        .append("$set", new Document()
            .append("name", deviceEntity.getName())
            .append("isTrusted", deviceEntity.isTrusted())
        );

    collection.updateOne(query, update);
  }
}
