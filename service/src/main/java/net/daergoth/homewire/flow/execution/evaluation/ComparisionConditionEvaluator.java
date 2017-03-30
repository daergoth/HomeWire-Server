package net.daergoth.homewire.flow.execution.evaluation;


import net.daergoth.homewire.controlpanel.LiveDataService;
import net.daergoth.homewire.flow.persistence.ConditionDTO;

public class ComparisionConditionEvaluator implements ConditionEvaluator {

  private final LiveDataService liveDataService;

  public ComparisionConditionEvaluator(LiveDataService liveDataService) {
    this.liveDataService = liveDataService;
  }

  @Override
  public boolean evaluateCondition(ConditionDTO conditionDTO) {
    Float threshold = Float.parseFloat(conditionDTO.getParameter());
    Float currentValue =
        liveDataService
            .getCurrentDeviceDataForIdAndType(conditionDTO.getDevId(), conditionDTO.getDevType())
            .getValue();

    switch (conditionDTO.getType()) {
      case "lessthan":
        return currentValue < threshold;
      case "lessthanequal":
        return currentValue <= threshold;
      case "greaterthan":
        return currentValue > threshold;
      case "greaterthanequal":
        return currentValue >= threshold;
      case "equal":
        return currentValue.equals(threshold);
      case "notequal":
        return !currentValue.equals(threshold);
      default:
        return false;
    }

  }

}
