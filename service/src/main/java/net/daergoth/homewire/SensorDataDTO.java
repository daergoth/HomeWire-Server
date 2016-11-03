package net.daergoth.homewire;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class SensorDataDTO<T> {

  private T data;

  @DateTimeFormat(pattern = "yyyy-MM-DD hh:mm:ss")
  private Date time;

  public SensorDataDTO(T data, Date time) {
    this.data = data;
    this.time = time;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }
}
