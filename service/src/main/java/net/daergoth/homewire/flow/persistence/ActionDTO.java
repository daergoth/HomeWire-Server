package net.daergoth.homewire.flow.persistence;

public class ActionDTO {

  public enum ActionTypes {
    SET,
    REQUEST,
    DELAY
  }

  private Short devId;

  private String devType;

  private String type;

  private ActionTypes actionType;

  private String parameter;

  public ActionDTO() {
  }

  public ActionDTO(Short devId, String devType, String type, String parameter) {
    this.devId = devId;
    this.devType = devType;
    this.type = type;
    this.actionType = typeStringToEnum(type);
    this.parameter = parameter;
  }

  public Short getDevId() {
    return devId;
  }

  public void setDevId(Short devId) {
    this.devId = devId;
  }

  public String getDevType() {
    return devType;
  }

  public void setDevType(String devType) {
    this.devType = devType;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
    this.actionType = typeStringToEnum(type);
  }

  public ActionTypes getActionType() {
    return actionType;
  }

  public void setActionType(ActionTypes actionType) {
    this.actionType = actionType;
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  private ActionTypes typeStringToEnum(String type) {
    switch (type) {
      case "set":
        return ActionTypes.SET;
      case "request":
        return ActionTypes.REQUEST;
      case "delay":
        return ActionTypes.DELAY;
    }

    return ActionTypes.SET;
  }
}
