package net.daergoth.homewire;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class SensorDataDTO<T> {

  private final Short id;

  private final String type;

  private final T value;

  @DateTimeFormat(pattern = "yyyy-MM-DD hh:mm:ss")
  private final Date time;

  public SensorDataDTO(Short id, String type, T value, Date time) {
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
