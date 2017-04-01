package net.daergoth.homewire;

public class DeviceCommand {

  private Short id;

  private Float targetState;

  public DeviceCommand(Short id, Float targetState) {
    this.id = id;
    this.targetState = targetState;
  }

  public Short getId() {
    return id;
  }

  public void setId(Short id) {
    this.id = id;
  }

  public Float getTargetState() {
    return targetState;
  }

  public void setTargetState(Float targetState) {
    this.targetState = targetState;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DeviceCommand that = (DeviceCommand) o;

    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }
    return targetState != null ? targetState.equals(that.targetState) : that.targetState == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (targetState != null ? targetState.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "DeviceCommand{" +
        "id=" + id +
        ", targetState=" + targetState +
        '}';
  }
}
