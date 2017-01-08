package net.daergoth.homewire.setup;

public class SensorEntity {

  private Short devId;

  private String name;

  private String type;

  private boolean isTrusted;

  public SensorEntity() {
  }

  public SensorEntity(Short devId, String name, String type, boolean isTrusted) {
    this.devId = devId;
    this.name = name;
    this.type = type;
    this.isTrusted = isTrusted;
  }

  public Short getDevId() {
    return devId;
  }

  public void setDevId(Short devId) {
    this.devId = devId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isTrusted() {
    return isTrusted;
  }

  public void setTrusted(boolean trusted) {
    isTrusted = trusted;
  }

  @Override
  public String toString() {
    return "SensorEntity{" +
        "devId=" + devId +
        ", name='" + name + '\'' +
        ", type='" + type + '\'' +
        ", isTrusted=" + isTrusted +
        '}';
  }
}
