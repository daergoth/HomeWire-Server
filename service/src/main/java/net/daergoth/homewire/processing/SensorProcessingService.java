package net.daergoth.homewire.processing;

import net.daergoth.homewire.live.LiveDataEntity;
import net.daergoth.homewire.live.LiveDataRepository;
import net.daergoth.homewire.statistic.SensorMeasurementEntity;
import net.daergoth.homewire.statistic.StatisticDataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorProcessingService {

  private final StatisticDataRepository statisticDataRepository;

  private final LiveDataRepository liveDataRepository;

  private final ModelMapper modelMapper;

  @Autowired
  public SensorProcessingService(StatisticDataRepository statisticDataRepository,
                                 ModelMapper modelMapper, LiveDataRepository liveDataRepository) {
    this.statisticDataRepository = statisticDataRepository;
    this.modelMapper = modelMapper;
    this.liveDataRepository = liveDataRepository;
  }

  public void processSensorData(ProcessableSensorDataDTO dataDTO) {
    statisticDataRepository.saveSensorData(modelMapper.map(dataDTO, SensorMeasurementEntity.class));

    liveDataRepository.saveLiveData(modelMapper.map(dataDTO, LiveDataEntity.class));
  }

}
