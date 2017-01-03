package net.daergoth.homewire.processing;

import java.time.ZonedDateTime;

public class ProcessableSensorDataDTO {

  private final Short id;

  private final String type;

  private final Float value;

  private final ZonedDateTime time;

  public ProcessableSensorDataDTO(Short id, String type, Float value, ZonedDateTime time) {
    this.id = id;
    this.type = type;
    this.value = value;
    this.time = time;
  }

  public Short getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public Float getValue() {
    return value;
  }

  public ZonedDateTime getTime() {
    return time;
  }

  @Override
  public String toString() {
    return "ProcessableSensorDataDTO{" +
        "id=" + id +
        ", type='" + type + '\'' +
        ", value=" + value +
        ", time=" + time +
        '}';
  }
}
