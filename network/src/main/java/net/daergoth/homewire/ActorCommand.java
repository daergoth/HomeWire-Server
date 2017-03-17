package net.daergoth.homewire;

public class ActorCommand {

  private Short id;

  private boolean targetState;

  public ActorCommand(Short id, boolean targetState) {
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
    return "ActorCommand{" +
        "id=" + id +
        ", targetState=" + targetState +
        '}';
  }
}
