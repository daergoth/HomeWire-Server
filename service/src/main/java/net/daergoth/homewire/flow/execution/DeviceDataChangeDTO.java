package net.daergoth.homewire.flow.execution;

public class DeviceDataChangeDTO {

  private Short id;

  private String type;

  private Float value;

  public DeviceDataChangeDTO() {
  }

  public DeviceDataChangeDTO(Short id, String type, Float value) {
    this.id = id;
    this.type = type;
    this.value = value;
  }

  public Short getId() {
    return id;
  }

  public void setId(Short id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Float getValue() {
    return value;
  }

  public void setValue(Float value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DeviceDataChangeDTO that = (DeviceDataChangeDTO) o;

    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }
    if (type != null ? !type.equals(that.type) : that.type != null) {
      return false;
    }
    return value != null ? value.equals(that.value) : that.value == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "DeviceDataChangeDTO{" +
        "id=" + id +
        ", type='" + type + '\'' +
        ", value=" + value +
        '}';
  }
}
