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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DeviceDTO deviceDTO = (DeviceDTO) o;

    if (isTrusted != deviceDTO.isTrusted) {
      return false;
    }
    if (!devId.equals(deviceDTO.devId)) {
      return false;
    }
    if (!category.equals(deviceDTO.category)) {
      return false;
    }
    if (!name.equals(deviceDTO.name)) {
      return false;
    }
    return type.equals(deviceDTO.type);
  }

  @Override
  public int hashCode() {
    int result = devId.hashCode();
    result = 31 * result + category.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + type.hashCode();
    result = 31 * result + (isTrusted ? 1 : 0);
    return result;
  }
}
