package net.daergoth.homewire;


public class SensorData {

  private Short id;

  private String type;

  private Float value;

  public Short getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public Float getValue() {
    return value;
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

  @Override
  public String toString() {
    return "SensorData{" +
        "id=" + id +
        ", type='" + type + '\'' +
        ", value=" + value +
        '}';
  }
}
