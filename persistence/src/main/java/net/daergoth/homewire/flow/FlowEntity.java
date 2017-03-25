package net.daergoth.homewire.flow;

import java.util.LinkedList;
import java.util.List;

public class FlowEntity {

  private Integer id;

  private String name;

  private Integer orderNum;

  private List<ConditionEntity> conditionList;

  private List<ActionEntity> actionList;

  public FlowEntity(Integer id, String name, Integer orderNum) {
    this.id = id;
    this.name = name;
    this.orderNum = orderNum;

    conditionList = new LinkedList<>();
    actionList = new LinkedList<>();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(Integer orderNum) {
    this.orderNum = orderNum;
  }

  public List<ConditionEntity> getConditionList() {
    return conditionList;
  }

  public List<ActionEntity> getActionList() {
    return actionList;
  }

  public void addCondition(ConditionEntity condition) {
    conditionList.add(condition);
  }

  public void addAction(ActionEntity action) {
    actionList.add(action);
  }

  @Override
  public String toString() {
    return "FlowEntity{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", orderNum=" + orderNum +
        '}';
  }
}
