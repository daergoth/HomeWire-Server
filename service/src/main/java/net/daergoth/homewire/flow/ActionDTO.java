package net.daergoth.homewire.flow;

public class ActionDTO {

  private Short devId;

  private String devType;

  private String type;

  private String parameter;

  public ActionDTO() {
  }

  public ActionDTO(Short devId, String devType, String type, String parameter) {
    this.devId = devId;
    this.devType = devType;
    this.type = type;
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
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }
}
