package net.daergoth.homewire;

public class DeviceCommand {

  private Short id;

  private boolean targetState;

  public DeviceCommand(Short id, boolean targetState) {
    this.id = id;
    this.targetState = targetState;
  }

  public Short getId() {
    return id;
  }

  public void setId(Short id) {
    this.id = id;
  }

  public boolean isTargetState() {
    return targetState;
  }

  public void setTargetState(boolean targetState) {
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
