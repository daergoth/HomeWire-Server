package net.daergoth.homewire.controlpanel;


public class LiveDataEntity {

  private Short id;

  private String type;

  private Float value;

  public LiveDataEntity() {
  }

  public LiveDataEntity(Short id, String type, Float value) {
    this.id = id;
    this.type = type;
    this.value = value;
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

  @Override
  public String toString() {
    return "LiveDataEntity{" +
        "id=" + id +
        ", type='" + type + '\'' +
        ", value=" + value +
        '}';
  }
}
