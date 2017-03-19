package net.daergoth.homewire.processing;

import java.time.ZonedDateTime;

public class ProcessableDeviceDataDTO {

  private final Short id;

  private final String category;

  private final String type;

  private final Float value;

  private final ZonedDateTime time;

  public ProcessableDeviceDataDTO(Short id, String category, String type, Float value,
                                  ZonedDateTime time) {
    this.id = id;
    this.category = category;
    this.type = type;
    this.value = value;
    this.time = time;
  }

  public Short getId() {
    return id;
  }

  public String getCategory() {
    return category;
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
    return "ProcessableDeviceDataDTO{" +
        "id=" + id +
        ", category='" + category + '\'' +
        ", type='" + type + '\'' +
        ", value=" + value +
        ", time=" + time +
        '}';
  }
}
