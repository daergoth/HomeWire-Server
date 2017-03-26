package net.daergoth.homewire.flow;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlowService {

  private final FlowRepository flowRepository;

  private final ModelMapper modelMapper;

  @Autowired
  public FlowService(FlowRepository flowRepository, ModelMapper modelMapper) {
    this.flowRepository = flowRepository;
    this.modelMapper = modelMapper;
  }

  public void saveFlowDto(FlowDTO flowDTO) {
    FlowEntity flowEntity = modelMapper.map(flowDTO, FlowEntity.class);
    flowDTO.getConditionList()
        .forEach(conditionDTO -> flowEntity.addCondition(modelMapper.map(conditionDTO, ConditionEntity.class)));
    flowDTO.getActionList()
        .forEach(actionDTO -> flowEntity.addAction(modelMapper.map(actionDTO, ActionEntity.class)));

    flowRepository.saveFlow(flowEntity);
  }

  public List<FlowDTO> getAllFlowDtos() {
    return flowRepository.getAllFlows().stream()
        .map(flowEntity -> {
          FlowDTO dto = modelMapper.map(flowEntity, FlowDTO.class);

          flowEntity.getConditionList()
              .forEach(conditionEntity ->
                  dto.addCondition(modelMapper.map(conditionEntity, ConditionDTO.class))
              );

          flowEntity.getActionList()
              .forEach(actionEntity ->
                dto.addAction(modelMapper.map(actionEntity, ActionDTO.class))
              );

          return dto;
        })
        .collect(Collectors.toList());
  }

}
