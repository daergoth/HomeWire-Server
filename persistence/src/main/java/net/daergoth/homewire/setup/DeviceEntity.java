package net.daergoth.homewire.setup;

public class DeviceEntity {

  private Short devId;

  private String name;

  private String category;

  private String type;

  private boolean isTrusted;

  public DeviceEntity() {
  }

  public DeviceEntity(Short devId, String name, String category, String type, boolean isTrusted) {
    this.devId = devId;
    this.name = name;
    this.category = category;
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

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
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
    return "DeviceEntity{" +
        "devId=" + devId +
        ", name='" + name + '\'' +
        ", category='" + category + '\'' +
        ", type='" + type + '\'' +
        ", isTrusted=" + isTrusted +
        '}';
  }
}
