package net.daergoth.homewire.processing;

import net.daergoth.homewire.live.LiveDataEntity;
import net.daergoth.homewire.live.LiveDataRepository;
import net.daergoth.homewire.setup.SensorEntity;
import net.daergoth.homewire.setup.SensorSetupRepository;
import net.daergoth.homewire.statistic.SensorMeasurementEntity;
import net.daergoth.homewire.statistic.StatisticDataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorProcessingService {

  private final StatisticDataRepository statisticDataRepository;

  private final LiveDataRepository liveDataRepository;

  private final SensorSetupRepository sensorSetupRepository;

  private final ModelMapper modelMapper;

  @Autowired
  public SensorProcessingService(StatisticDataRepository statisticDataRepository,
                                 ModelMapper modelMapper, LiveDataRepository liveDataRepository,
                                 SensorSetupRepository sensorSetupRepository) {
    this.statisticDataRepository = statisticDataRepository;
    this.modelMapper = modelMapper;
    this.liveDataRepository = liveDataRepository;
    this.sensorSetupRepository = sensorSetupRepository;
  }

  public void processSensorData(ProcessableSensorDataDTO dataDTO) {
    statisticDataRepository.saveSensorData(modelMapper.map(dataDTO, SensorMeasurementEntity.class));

    liveDataRepository.saveLiveData(modelMapper.map(dataDTO, LiveDataEntity.class));

    //TODO proper init sensor message
    if (sensorSetupRepository.getSensorEntityByIdAndType(dataDTO.getId(), dataDTO.getType())
        == null) {
      sensorSetupRepository.saveSensorEntity(
          new SensorEntity(
              dataDTO.getId(),
              "Unknown - " + dataDTO.getType() + dataDTO.getId(),
              dataDTO.getType(),
              false
          )
      );
    }
  }

}
