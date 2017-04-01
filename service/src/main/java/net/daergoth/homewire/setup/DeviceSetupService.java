package net.daergoth.homewire.setup;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeviceSetupService {

  private final DeviceSetupRepository deviceSetupRepository;

  private final ModelMapper modelMapper;

  @Autowired
  public DeviceSetupService(DeviceSetupRepository deviceSetupRepository, ModelMapper modelMapper) {
    this.deviceSetupRepository = deviceSetupRepository;
    this.modelMapper = modelMapper;
  }

  public void assignNameToId(Short devId, String type, String name) {
    deviceSetupRepository.assignNameToDevice(devId, type, name);
  }

  public void setTrustedDevice(Short devId, String type) {
    deviceSetupRepository.setTrustedStatusToDevice(devId, type, true);
  }

  public void setUntrustedDevice(Short devId, String type) {
    deviceSetupRepository.setTrustedStatusToDevice(devId, type, false);
  }

  public List<String> getSensorTypes() {
    return deviceSetupRepository.getSensorTypes();
  }

  public List<DeviceDTO> getAllDeviceDtos() {
    return deviceSetupRepository.getAllDeviceEntities()
        .stream()
        .map(deviceEntity -> modelMapper.map(deviceEntity, DeviceDTO.class))
        .collect(Collectors.toList());
  }

  public List<DeviceDTO> getAllSensors() {
    return deviceSetupRepository.getAllSensorEntities()
        .stream()
        .map(deviceEntity -> modelMapper.map(deviceEntity, DeviceDTO.class))
        .collect(Collectors.toList());
  }

  public List<DeviceDTO> getAllActors() {
    return deviceSetupRepository.getAllActorEntities()
        .stream()
        .map(deviceEntity -> modelMapper.map(deviceEntity, DeviceDTO.class))
        .collect(Collectors.toList());
  }

  public Map<String, List<DeviceDTO>> getSensorDtosGroupedByType() {
    return deviceSetupRepository.getAllDeviceEntities()
        .stream()
        .map(deviceEntity -> modelMapper.map(deviceEntity, DeviceDTO.class))
        .collect(Collectors.groupingBy(DeviceDTO::getType, Collectors.toList()));
  }

  public DeviceDTO getDeviceDtoByIdAndType(Short devId, String type) {
    DeviceEntity entity = deviceSetupRepository.getDeviceEntityByIdAndType(devId, type);
    if (entity == null) {
      return null;
    } else {
      return modelMapper.map(entity, DeviceDTO.class);
    }
  }

  public String getDeviceNameByIdAndType(Short devId, String type) {
    DeviceEntity deviceEntity = deviceSetupRepository.getDeviceEntityByIdAndType(devId, type);
    if (deviceEntity == null) {
      return null;
    }
    return deviceEntity.getName();
  }

  public List<DeviceDTO> getDeviceDtosByName(String name) {
    return deviceSetupRepository.getDeviceEntitiesByName(name)
        .stream()
        .map(deviceEntity -> modelMapper.map(deviceEntity, DeviceDTO.class))
        .collect(Collectors.toList());
  }

  public List<DeviceDTO> getDeviceDtosByType(String type) {
    return deviceSetupRepository.getDeviceEntitiesByType(type)
        .stream()
        .map(deviceEntity -> modelMapper.map(deviceEntity, DeviceDTO.class))
        .collect(Collectors.toList());
  }

  public void updateDeviceDto(DeviceDTO deviceDTO) {
    deviceSetupRepository.updateDeviceEntity(modelMapper.map(deviceDTO, DeviceEntity.class));
  }

  public void removeDeviceDtoByDevIdAndDevType(Short devId, String devType) {
    deviceSetupRepository.removeDeviceDtoByDevIdAndDevType(devId, devType);
  }

}
