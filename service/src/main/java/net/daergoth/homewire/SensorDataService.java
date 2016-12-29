package net.daergoth.homewire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorDataService {

  private final SensorDataRepository sensorDataRepository;

  @Autowired
  public SensorDataService(SensorDataRepository sensorDataRepository) {
    this.sensorDataRepository = sensorDataRepository;
  }

  public List<SensorDataDTO<?>> getSensorData() {
    return sensorDataRepository.getSensorData().stream()
        .map(e -> new SensorDataDTO<>(e.getId(), e.getType(), e.getValue(), e.getTime()))
        .collect(Collectors.toList());
  }

}
