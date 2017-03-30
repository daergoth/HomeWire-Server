package net.daergoth.homewire.flow.execution;

public class DeviceIdentifier {
  private Short id;

  private String type;

  public DeviceIdentifier() {
  }

  public DeviceIdentifier(Short id, String type) {
    this.id = id;
    this.type = type;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DeviceIdentifier that = (DeviceIdentifier) o;

    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }
    return type != null ? type.equals(that.type) : that.type == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (type != null ? type.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "DeviceIdentifier{" +
        "id=" + id +
        ", type='" + type + '\'' +
        '}';
  }
}