package net.daergoth.homewire.flow;

import java.util.LinkedList;
import java.util.List;

public class FlowDTO {

  private Integer id;

  private String name;

  private Integer orderNum;

  private List<ConditionDTO> conditionList;

  private List<ActionDTO> actionList;

  public FlowDTO() {
    conditionList = new LinkedList<>();
    actionList = new LinkedList<>();
  }

  public FlowDTO(Integer id, String name, Integer orderNum) {
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

  public List<ConditionDTO> getConditionList() {
    return conditionList;
  }

  public List<ActionDTO> getActionList() {
    return actionList;
  }

  public void addCondition(ConditionDTO condition) {
    conditionList.add(condition);
  }

  public void addCondition(int index, ConditionDTO condition) {
    conditionList.add(index, condition);
  }

  public void removeCondition(ConditionDTO condition) {
    conditionList.remove(condition);
  }

  public void addAction(ActionDTO action) {
    actionList.add(action);
  }

  public void addAction(int index, ActionDTO action) {
    actionList.add(index, action);
  }

  public void removeAction(ActionDTO action) {
    actionList.remove(action);
  }

  @Override
  public String toString() {
    return "FlowDTO{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", orderNum=" + orderNum +
        "}";
  }
}
