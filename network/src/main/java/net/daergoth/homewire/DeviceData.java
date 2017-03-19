package net.daergoth.homewire;


public class DeviceData {

  private Short id;

  private String category;

  private String type;

  private Float value;

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

  public void setId(Short id) {
    this.id = id;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setValue(Float value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "DeviceData{" +
        "id=" + id +
        ", category='" + category + '\'' +
        ", type='" + type + '\'' +
        ", value=" + value +
        '}';
  }
}
