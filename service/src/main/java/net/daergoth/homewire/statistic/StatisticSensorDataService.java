package net.daergoth.homewire.statistic;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

  public List<StatisticDataDTO> getStats(SensorMeasurementEntity.MeasurementInterval measurementInterval) {
    return statisticDataRepository
        .getSensorDataWithInterval(measurementInterval)
        .stream()
        .map(entity -> modelMapper.map(entity, StatisticDataDTO.class))
        .collect(Collectors.toList());
  }

}
