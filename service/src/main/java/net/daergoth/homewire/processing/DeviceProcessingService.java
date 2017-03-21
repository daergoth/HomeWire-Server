package net.daergoth.homewire.processing;

import net.daergoth.homewire.controlpanel.LiveDataEntity;
import net.daergoth.homewire.controlpanel.LiveDataRepository;
import net.daergoth.homewire.setup.DeviceEntity;
import net.daergoth.homewire.setup.DeviceSetupRepository;
import net.daergoth.homewire.statistic.DeviceStateEntity;
import net.daergoth.homewire.statistic.StatisticDataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceProcessingService {

  private final StatisticDataRepository statisticDataRepository;

  private final LiveDataRepository liveDataRepository;

  private final DeviceSetupRepository deviceSetupRepository;

  private final ModelMapper modelMapper;

  @Autowired
  public DeviceProcessingService(StatisticDataRepository statisticDataRepository,
                                 ModelMapper modelMapper, LiveDataRepository liveDataRepository,
                                 DeviceSetupRepository deviceSetupRepository) {
    this.statisticDataRepository = statisticDataRepository;
    this.modelMapper = modelMapper;
    this.liveDataRepository = liveDataRepository;
    this.deviceSetupRepository = deviceSetupRepository;
  }

  public void processDeviceData(ProcessableDeviceDataDTO dataDTO) {
    statisticDataRepository.saveDeviceState(modelMapper.map(dataDTO, DeviceStateEntity.class));

    liveDataRepository.saveLiveData(modelMapper.map(dataDTO, LiveDataEntity.class));

    if (deviceSetupRepository.getDeviceEntityByIdAndType(dataDTO.getId(), dataDTO.getType())
        == null) {
      deviceSetupRepository.saveDeviceEntity(
          new DeviceEntity(
              dataDTO.getId(),
              "Unknown - " + dataDTO.getType() + dataDTO.getId(),
              dataDTO.getCategory(),
              dataDTO.getType(),
              false
          )
      );
    }
  }

}
