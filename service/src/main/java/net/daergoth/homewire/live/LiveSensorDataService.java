package net.daergoth.homewire.live;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LiveSensorDataService {

  private final LiveDataRepository liveDataRepository;

  private final ModelMapper modelMapper;

  @Autowired
  public LiveSensorDataService(LiveDataRepository liveDataRepository, ModelMapper modelMapper) {
    this.liveDataRepository = liveDataRepository;
    this.modelMapper = modelMapper;
  }

  public List<LiveDataDTO> getCurrentSensorData() {
    return liveDataRepository.getLiveData().stream()
        .map(liveDataEntity -> modelMapper.map(liveDataEntity, LiveDataDTO.class))
        .collect(Collectors.toList());
  }
}
