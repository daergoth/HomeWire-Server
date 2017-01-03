package net.daergoth.homewire.live;

public class LiveDataDTO {

  private Short id;

  private String type;

  private Float value;

  public LiveDataDTO() {
  }

  public LiveDataDTO(Short id, String type, Float value) {
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
}
