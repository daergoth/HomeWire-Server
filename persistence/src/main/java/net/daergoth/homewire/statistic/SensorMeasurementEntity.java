package net.daergoth.homewire.statistic;

import java.time.ZonedDateTime;

public class SensorMeasurementEntity {

  public enum MeasurementInterval {
    CURRENT,
    MINUTE,
    HOUR,
    DAY
  }

  private Short id;

  private String type;

  private Float value;

  private ZonedDateTime time;

  private MeasurementInterval interval = MeasurementInterval.CURRENT;

  public SensorMeasurementEntity() {
  }

  public SensorMeasurementEntity(Short id, String type, Float value, ZonedDateTime time) {
    this.id = id;
    this.type = type;
    this.value = value;
    this.time = time;
  }

  public SensorMeasurementEntity(Short id, String type, Float value, ZonedDateTime time,
                                 MeasurementInterval interval) {
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
}
