package net.daergoth.homewire.statistic;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticSensorDataService {

  private final StatisticDataRepository statisticDataRepository;

  private final ModelMapper modelMapper;

  @Autowired
  public StatisticSensorDataService(StatisticDataRepository statisticDataRepository,
                                    ModelMapper modelMapper) {
    this.statisticDataRepository = statisticDataRepository;
    this.modelMapper = modelMapper;
  }

  public List<StatisticDataDTO> getStatByMinute(String sensorType) {
    return statisticDataRepository
        .getSensorDataWithInterval(sensorType, SensorMeasurementEntity.MeasurementInterval.MINUTE)
        .stream()
        .map(entity -> modelMapper.map(entity, StatisticDataDTO.class))
        .collect(Collectors.toList());
  }

  public List<StatisticDataDTO> getStatByHour(String sensorType) {
    return statisticDataRepository
        .getSensorDataWithInterval(sensorType, SensorMeasurementEntity.MeasurementInterval.HOUR)
        .stream()
        .map(entity -> modelMapper.map(entity, StatisticDataDTO.class))
        .collect(Collectors.toList());
  }

  public List<StatisticDataDTO> getStatByDay(String sensorType) {
    return statisticDataRepository
        .getSensorDataWithInterval(sensorType, SensorMeasurementEntity.MeasurementInterval.DAY)
        .stream()
        .map(entity -> modelMapper.map(entity, StatisticDataDTO.class))
        .collect(Collectors.toList());
  }

}
