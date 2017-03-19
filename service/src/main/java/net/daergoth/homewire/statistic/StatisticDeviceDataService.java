package net.daergoth.homewire.statistic;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticDeviceDataService {

  private final StatisticDataRepository statisticDataRepository;

  private final ModelMapper modelMapper;

  @Autowired
  public StatisticDeviceDataService(StatisticDataRepository statisticDataRepository,
                                    ModelMapper modelMapper) {
    this.statisticDataRepository = statisticDataRepository;
    this.modelMapper = modelMapper;
  }

  public List<StatisticDataDTO> getStats(DeviceStateEntity.StateInterval stateInterval) {
    return statisticDataRepository
        .getDeviceStateWithInterval(stateInterval)
        .stream()
        .map(entity -> modelMapper.map(entity, StatisticDataDTO.class))
        .collect(Collectors.toList());
  }

}
