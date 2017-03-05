package net.daergoth.homewire.statistic;

import java.time.ZonedDateTime;

public class StatisticDataDTO {

  private Short id;

  private String type;

  private Float value;

  private ZonedDateTime time;


  public StatisticDataDTO() {
  }

  public StatisticDataDTO(Short id, String type, Float value, ZonedDateTime time) {
    this.id = id;
    this.type = type;
    this.value = value;
    this.time = time;
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

  public Float getValue() {
    return value;
  }

  public void setValue(Float value) {
    this.value = value;
  }

  public ZonedDateTime getTime() {
    return time;
  }

  public void setTime(ZonedDateTime time) {
    this.time = time;
  }

  @Override
  public String toString() {
    return "StatisticDataDTO{" +
        "id=" + id +
        ", type='" + type + '\'' +
        ", value=" + value +
        ", time=" + time +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    StatisticDataDTO that = (StatisticDataDTO) o;

    if (!id.equals(that.id)) return false;
    if (!type.equals(that.type)) return false;
    if (!value.equals(that.value)) return false;
    return time.equals(that.time);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + type.hashCode();
    result = 31 * result + value.hashCode();
    result = 31 * result + time.hashCode();
    return result;
  }
}
