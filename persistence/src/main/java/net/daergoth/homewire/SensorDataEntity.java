package net.daergoth.homewire;

import java.util.Date;

public class SensorDataEntity<T> {

  private final Short id;

  private final String type;

  private final T value;

  private final Date time;

  public SensorDataEntity(Short id, String type, T value, Date time) {
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

  public T getValue() {
    return value;
  }

  public Date getTime() {
    return time;
  }
}
