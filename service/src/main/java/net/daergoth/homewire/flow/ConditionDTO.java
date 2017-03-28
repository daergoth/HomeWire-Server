package net.daergoth.homewire.flow;

public class ConditionDTO {

  public enum ConditionTypes {
    Comparision,
    Request,
    Interface,
    Time
  }

  private Short devId;

  private String devType;

  private String type;

  private ConditionTypes conditionType;

  private String parameter;

  public ConditionDTO() {
  }

  public ConditionDTO(Short devId, String devType, String type, String parameter) {
    this.devId = devId;
    this.devType = devType;
    this.type = type;
    this.conditionType = typeStringToEnum(type);
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
    this.conditionType = typeStringToEnum(type);
  }

  public ConditionTypes getConditionType() {
    return conditionType;
  }

  public void setConditionType(ConditionTypes conditionType) {
    this.conditionType = conditionType;
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  private ConditionTypes typeStringToEnum(String type) {
    switch (type) {
      case "request":
        return ConditionTypes.Request;
      case "interface":
        return ConditionTypes.Interface;
      case "time":
        return ConditionTypes.Time;
      default:
        return ConditionTypes.Comparision;
    }
  }

}
