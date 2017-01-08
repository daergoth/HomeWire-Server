package net.daergoth.homewire.setup;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorSetupService {

  private final SensorSetupRepository sensorSetupRepository;

  private final ModelMapper modelMapper;

  @Autowired
  public SensorSetupService(SensorSetupRepository sensorSetupRepository, ModelMapper modelMapper) {
    this.sensorSetupRepository = sensorSetupRepository;
    this.modelMapper = modelMapper;
  }

  public void assignNameToId(Short devId, String type, String name) {
    sensorSetupRepository.assignNameToSensor(devId, type, name);
  }

  public void setTrustedSensor(Short devId, String type) {
    sensorSetupRepository.setTrustedStatusToSensor(devId, type, true);
  }

  public void setUntrustedSensor(Short devId, String type) {
    sensorSetupRepository.setTrustedStatusToSensor(devId, type, false);
  }

  public List<String> getSensorTypes() {
    return sensorSetupRepository.getSensorTypes();
  }

  public List<SensorDTO> getAllSensorDtos() {
    return sensorSetupRepository.getAllSensorEntities()
        .stream()
        .map(sensorEntity -> modelMapper.map(sensorEntity, SensorDTO.class))
        .collect(Collectors.toList());
  }

  public SensorDTO getSensorDtoByIdAndType(Short devId, String type) {
    return modelMapper
        .map(sensorSetupRepository.getSensorEntityByIdAndType(devId, type), SensorDTO.class);
  }

  public String getSensorNameByIdAndType(Short devId, String type) {
    return sensorSetupRepository.getSensorEntityByIdAndType(devId, type).getName();
  }

  public List<SensorDTO> getSensorDtosByName(String name) {
    return sensorSetupRepository.getSensorEntityByName(name)
        .stream()
        .map(sensorEntity -> modelMapper.map(sensorEntity, SensorDTO.class))
        .collect(Collectors.toList());
  }

  public List<SensorDTO> getSensorDtosByType(String type) {
    return sensorSetupRepository.getSensorEntitiesByType(type)
        .stream()
        .map(sensorEntity -> modelMapper.map(sensorEntity, SensorDTO.class))
        .collect(Collectors.toList());
  }

  public void updateSensorDto(SensorDTO sensorDTO) {
    sensorSetupRepository.updateSensorEntity(modelMapper.map(sensorDTO, SensorEntity.class));
  }

}
