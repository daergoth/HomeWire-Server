package net.daergoth.homewire.flow;

import com.mongodb.client.MongoDatabase;
import net.daergoth.homewire.CustomMongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FlowRepository extends CustomMongoRepository {

  public static final String COLLECTION_NAME = "flows";

  private static Logger logger = LoggerFactory.getLogger(FlowRepository.class);

  @Autowired
  public FlowRepository(MongoDatabase db) {
    super(db);
  }

  @Override
  protected String getCollectionName() {
    return COLLECTION_NAME;
  }

  

}
