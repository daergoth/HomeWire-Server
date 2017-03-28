package net.daergoth.homewire.flow;

public class DeviceViewDTO {
  private Short devId;

  private String type;

  private String name;

  public DeviceViewDTO() {
  }

  public DeviceViewDTO(Short devId, String type, String name) {
    this.devId = devId;
    this.type = type;
    this.name = name;
  }

  public Short getDevId() {
    return devId;
  }

  public String getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public void setDevId(Short devId) {
    this.devId = devId;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DeviceViewDTO that = (DeviceViewDTO) o;

    if (devId != null ? !devId.equals(that.devId) : that.devId != null) {
      return false;
    }
    if (type != null ? !type.equals(that.type) : that.type != null) {
      return false;
    }
    return name != null ? name.equals(that.name) : that.name == null;
  }

  @Override
  public int hashCode() {
    int result = devId != null ? devId.hashCode() : 0;
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return name +
        " - " +
        type.substring(0, 1).toUpperCase() + type.substring(1);
  }
}
