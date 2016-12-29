package net.daergoth.homewire;

import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Repository
public class SensorDataRepository {

  private static final int LIMIT = 10;

  private List<SensorDataEntity<?>> dataList;

  public SensorDataRepository() {
    dataList = new LinkedList<>();
  }

  public List<SensorDataEntity<?>> getSensorData() {
    return Collections.unmodifiableList(dataList);
  }

  public void addData(SensorDataEntity<?> data) {
    dataList.add(data);

    if (dataList.size() > LIMIT) {
      dataList.remove(0);
    }
  }
}
