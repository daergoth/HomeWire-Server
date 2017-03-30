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
  public String toString() {
    return "DeviceCommand{" +
        "id=" + id +
        ", targetState=" + targetState +
        '}';
  }
}
