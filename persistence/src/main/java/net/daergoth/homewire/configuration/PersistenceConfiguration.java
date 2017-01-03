package net.daergoth.homewire.configuration;

import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

@Configuration
public class PersistenceConfiguration {

  private static final String DB_NAME = "homewire";

  @Bean
  public MongoClient mongoClient() throws UnknownHostException {
    MongoClient mongoClient = new MongoClient();
    return mongoClient;
  }

  @Bean
  public MongoDatabase db(MongoClient mongoClient) {
    MongoDatabase db = mongoClient.getDatabase(DB_NAME).withWriteConcern(WriteConcern.JOURNALED);
    return db;
  }

}
