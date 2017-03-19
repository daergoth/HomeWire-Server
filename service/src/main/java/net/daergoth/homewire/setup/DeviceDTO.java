package net.daergoth.homewire.setup;

public class DeviceDTO {

  private Short devId;

  private String category;

  private String name;

  private String type;

  private boolean isTrusted;

  public DeviceDTO() {
  }

  public DeviceDTO(Short devId, String category, String name, String type, boolean isTrusted) {
    this.devId = devId;
    this.category = category;
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

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
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
}
