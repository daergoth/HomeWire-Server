package net.daergoth.homewire;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public abstract class CustomMongoRepository {

  protected MongoCollection<Document> collection;

  public CustomMongoRepository(MongoDatabase db) {
    this.collection = db.getCollection(getCollectionName());
  }

  protected abstract String getCollectionName();


}
