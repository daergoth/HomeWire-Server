package net.daergoth.homewire.flow.persistence;

import net.daergoth.homewire.flow.ActionEntity;
import net.daergoth.homewire.flow.ConditionEntity;
import net.daergoth.homewire.flow.FlowEntity;
import net.daergoth.homewire.flow.FlowRepository;
import net.daergoth.homewire.flow.execution.FlowExecutorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public class FlowService {

  private final FlowRepository flowRepository;

  private final FlowExecutorService flowExecutorService;

  private final ModelMapper modelMapper;

  public FlowService(FlowRepository flowRepository, FlowExecutorService flowExecutorService,
                     ModelMapper modelMapper) {
    this.flowRepository = flowRepository;
    this.flowExecutorService = flowExecutorService;
    this.modelMapper = modelMapper;
  }

  public void saveFlowDto(FlowDTO flowDTO) {
    FlowEntity flowEntity = modelMapper.map(flowDTO, FlowEntity.class);
    flowDTO.getConditionList()
        .forEach(conditionDTO -> flowEntity
            .addCondition(modelMapper.map(conditionDTO, ConditionEntity.class)));
    flowDTO.getActionList()
        .forEach(actionDTO -> flowEntity.addAction(modelMapper.map(actionDTO, ActionEntity.class)));

    flowRepository.saveFlow(flowEntity);

    flowExecutorService.processFlowChange(flowDTO);
  }

  public void removeFlowDto(Integer flowId) {
    flowRepository.removeFlow(flowId);

    flowExecutorService.processFlowDelete(flowId);
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
