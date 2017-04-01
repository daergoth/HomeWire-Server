package net.daergoth.homewire.controlpanel;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LiveDataService {

  private final LiveDataRepository liveDataRepository;

  private final ModelMapper modelMapper;

  @Autowired
  public LiveDataService(LiveDataRepository liveDataRepository, ModelMapper modelMapper) {
    this.liveDataRepository = liveDataRepository;
    this.modelMapper = modelMapper;
  }

  public List<LiveDataDTO> getCurrentDeviceData() {
    return liveDataRepository.getLiveData().stream()
        .map(liveDataEntity -> modelMapper.map(liveDataEntity, LiveDataDTO.class))
        .collect(Collectors.toList());
  }

  public LiveDataDTO getCurrentDeviceDataForIdAndType(Short deviceId, String deviceType) {
    return modelMapper
        .map(liveDataRepository.getCurrentDeviceDataForIdAndType(deviceId, deviceType),
            LiveDataDTO.class);
  }

  public void removeCurrentDeviceDataForDevIdAndDevType(Short devId, String devType) {
    liveDataRepository.removeCurrentDeviceDataForDevIdAndDevType(devId, devType);
  }
}
