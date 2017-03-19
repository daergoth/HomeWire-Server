package net.daergoth.homewire.statistic;

import java.time.ZonedDateTime;

public class DeviceStateEntity {

  public enum StateInterval {
    CURRENT,
    MINUTE,
    HOUR,
    DAY
  }

  private Short id;

  private String type;

  private Float value;

  private ZonedDateTime time;

  private StateInterval interval = StateInterval.CURRENT;

  public DeviceStateEntity() {
  }

  public DeviceStateEntity(Short id, String type, Float value, ZonedDateTime time) {
    this.id = id;
    this.type = type;
    this.value = value;
    this.time = time;
  }

  public DeviceStateEntity(Short id, String type, Float value, ZonedDateTime time,
                           StateInterval interval) {
    this.id = id;
    this.type = type;
    this.value = value;
    this.time = time;
    this.interval = interval;
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

  public void setId(Short id) {
    this.id = id;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setValue(Float value) {
    this.value = value;
  }

  public void setTime(ZonedDateTime time) {
    this.time = time;
  }

  @Override
  public String toString() {
    return "DeviceStateEntity{" +
        "id=" + id +
        ", type='" + type + '\'' +
        ", value=" + value +
        ", time=" + time +
        ", interval=" + interval +
        '}';
  }
}
