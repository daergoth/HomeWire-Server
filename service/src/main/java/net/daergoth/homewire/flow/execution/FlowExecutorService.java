package net.daergoth.homewire.flow.execution;

import net.daergoth.homewire.flow.execution.evaluation.ConditionEvaluatingService;
import net.daergoth.homewire.flow.execution.execution.ActionExecutingService;
import net.daergoth.homewire.flow.persistence.ActionDTO;
import net.daergoth.homewire.flow.persistence.ConditionDTO;
import net.daergoth.homewire.flow.persistence.FlowDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlowExecutorService {

  public static final Logger logger = LoggerFactory.getLogger(FlowExecutorService.class);

  private final ConditionEvaluatingService conditionEvaluatingService;

  private final ActionExecutingService actionExecutingService;

  private final Map<DeviceIdentifier, List<FlowDTO>> deviceToFlowMap;

  @Autowired
  public FlowExecutorService(ConditionEvaluatingService conditionEvaluatingService,
                             ActionExecutingService actionExecutingService) {
    this.conditionEvaluatingService = conditionEvaluatingService;
    this.actionExecutingService = actionExecutingService;

    this.deviceToFlowMap = new HashMap<>();
  }

  public void processDeviceDataChange(DeviceDataChangeDTO dataChangeDTO) {
    DeviceIdentifier deviceIdentifier =
        new DeviceIdentifier(dataChangeDTO.getId(), dataChangeDTO.getType());

    logger.info("Processing device data change for {}", deviceIdentifier);

    if (deviceToFlowMap.containsKey(deviceIdentifier)) {
      for (FlowDTO flowDTO : deviceToFlowMap.get(deviceIdentifier)) {
        if (evaluateFlow(flowDTO)) {
          executeFlow(flowDTO);
        }
      }
    }
  }

  public void processFlowDelete(Integer flowId) {
    deviceToFlowMap.values().forEach(flowDTOList -> {
      for (FlowDTO dto : flowDTOList) {
        if (dto.getId().equals(flowId)) {
          flowDTOList.remove(dto);
          break;
        }
      }
    });
  }

  public void processFlowChange(FlowDTO flowDTO) {
    deviceToFlowMap.values().forEach(flowDTOList -> {
      for (FlowDTO dto : flowDTOList) {
        if (dto.getId().equals(flowDTO.getId())) {
          flowDTOList.remove(dto);
          break;
        }
      }
    });

    flowDTO.getConditionList().forEach(conditionDTO -> {
      List<FlowDTO> flowDTOList = deviceToFlowMap.computeIfAbsent(
          new DeviceIdentifier(conditionDTO.getDevId(), conditionDTO.getDevType()),
          deviceIdentifier -> new ArrayList<>()
      );

      flowDTOList.add(flowDTO);

      flowDTOList.sort((flow1, flow2) -> 0 - flow1.getOrderNum().compareTo(flow2.getOrderNum()));
    });
  }

  private boolean evaluateFlow(FlowDTO flowDTO) {
    for (ConditionDTO conditionDTO : flowDTO.getConditionList()) {
      if (!conditionEvaluatingService.evaluateCondition(conditionDTO)) {
        logger.info("Evaluated flow {} to false", flowDTO);
        return false;
      }
    }
    logger.info("Evaluated flow {} to true", flowDTO);
    return true;
  }

  private void executeFlow(FlowDTO flowDTO) {
    logger.info("Executing flow {}", flowDTO);
    for (ActionDTO actionDTO : flowDTO.getActionList()) {
      actionExecutingService.executeAction(actionDTO);
    }
  }


}
