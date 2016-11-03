package net.daergoth.homewire;

import java.util.Date;

public class SensorDataEntity<T> {

  private T data;

  private Date time;

  public SensorDataEntity(T data, Date time) {
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
