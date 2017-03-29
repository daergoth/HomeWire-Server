package net.daergoth.homewire.flow;

import com.mongodb.Block;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import net.daergoth.homewire.CustomMongoRepository;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

  public void saveFlow(FlowEntity flowEntity) {

    if (flowEntity.getId() == null) {
      Integer nextId = collection.find()
          .projection(new Document("flow_id", 1))
          .sort(new Document("flow_id", -1))
          .limit(1)
          .first()
          .getInteger("flow_id") + 1;

      flowEntity.setId(nextId);
    }

    Document filter = new Document()
        .append("flow_id", flowEntity.getId());

    List<Document> conditionList = flowEntity.getConditionList()
        .stream()
        .map(conditionEntity ->
            new Document()
                .append("dev_id", conditionEntity.getDevId())
                .append("dev_type", conditionEntity.getDevType())
                .append("type", conditionEntity.getType())
                .append("parameter", conditionEntity.getParameter()))
        .collect(Collectors.toList());

    List<Document> actionList = flowEntity.getActionList()
        .stream()
        .map(conditionEntity ->
            new Document()
                .append("dev_id", conditionEntity.getDevId())
                .append("dev_type", conditionEntity.getDevType())
                .append("type", conditionEntity.getType())
                .append("parameter", conditionEntity.getParameter()))
        .collect(Collectors.toList());

    Document entityDocument = new Document()
        .append("$set", new Document()
            .append("flow_id", flowEntity.getId())
            .append("name", flowEntity.getName())
            .append("order_num", flowEntity.getOrderNum())
            .append("conditions", conditionList)
            .append("actions", actionList)
        );

    FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
        .returnDocument(ReturnDocument.AFTER)
        .upsert(true);

    collection.findOneAndUpdate(filter, entityDocument, options);
  }

  public void removeFlow(Integer flowId) {
    collection.deleteOne(new Document("flow_id", flowId));
  }

  public List<FlowEntity> getAllFlows() {
    List<FlowEntity> result = new ArrayList<>();

    collection.find().forEach((Block<? super Document>) document -> {
      FlowEntity flowEntity = new FlowEntity(
          document.getInteger("flow_id"),
          document.getString("name"),
          document.getInteger("order_num")
      );

      List<Document> conditions = (List<Document>) document.get("conditions");
      conditions.forEach(conditionDocument -> {
        flowEntity.addCondition(new ConditionEntity(
            conditionDocument.getInteger("dev_id").shortValue(),
            conditionDocument.getString("dev_type"),
            conditionDocument.getString("type"),
            conditionDocument.getString("parameter")
        ));
      });

      List<Document> actions = (List<Document>) document.get("actions");
      actions.forEach(actionDocument -> {
        flowEntity.addAction(new ActionEntity(
            actionDocument.getInteger("dev_id").shortValue(),
            actionDocument.getString("dev_type"),
            actionDocument.getString("type"),
            actionDocument.getString("parameter")
        ));
      });

      result.add(flowEntity);
    });

    return result;
  }

}
