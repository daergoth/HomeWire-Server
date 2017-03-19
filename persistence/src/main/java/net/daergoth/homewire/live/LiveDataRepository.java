package net.daergoth.homewire.live;

import com.mongodb.client.MongoDatabase;
import net.daergoth.homewire.CustomMongoRepository;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class LiveDataRepository extends CustomMongoRepository {

  private final Logger logger = LoggerFactory.getLogger(LiveDataRepository.class);

  private static final String COLLECTION_NAME = "live_data";

  @Autowired
  public LiveDataRepository(MongoDatabase db) {
    super(db);
  }

  @Override
  protected String getCollectionName() {
    return COLLECTION_NAME;
  }

  public void saveLiveData(LiveDataEntity liveDataEntity) {
    logger.info("Saving live data: {}", liveDataEntity);

    if (liveDataEntity.getValue() == null) {
      logger.warn("Live device data with null value!");

      return;
    }

    Document query = new Document()
        .append("type", liveDataEntity.getType());

    if (collection.count(query) > 0) {
      Document updated = new Document()
          .append("$set", new Document(
              "values." + String.valueOf(liveDataEntity.getId()), liveDataEntity.getValue()));

      collection.updateOne(query, updated);
    } else {
      Document document = new Document()
          .append("type", liveDataEntity.getType())
          .append("values", new Document()
              .append(String.valueOf(liveDataEntity.getId()), liveDataEntity.getValue())
          );
      collection.insertOne(document);
    }
  }

  public List<LiveDataEntity> getLiveData() {
    List<Document> docs = collection.find().into(new ArrayList<>());

    List<LiveDataEntity> result = new ArrayList<>();

    for (Document d : docs) {
      String type = d.getString("type");
      Document values = (Document) d.get("values");

      for (Map.Entry<String, Object> entry : values.entrySet()) {
        Short devId = Short.parseShort(entry.getKey());
        Double value = (Double) entry.getValue();

        result.add(new LiveDataEntity(devId, type, value.floatValue()));
      }
    }

    return result;
  }
}
