package net.daergoth.homewire.flow.execution.evaluation;

import net.daergoth.homewire.flow.persistence.ConditionDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConditionEvaluatingService {

  private Map<ConditionDTO.ConditionTypes, ConditionEvaluator> evaluatorMap;

  public ConditionEvaluatingService() {
    this.evaluatorMap = new HashMap<>();
  }

  public boolean evaluateCondition(ConditionDTO conditionDTO) {
    if (evaluatorMap.containsKey(conditionDTO.getConditionType())) {
      return evaluatorMap.get(conditionDTO.getConditionType()).evaluateCondition(conditionDTO);
    }

    return false;
  }

  public ConditionEvaluatingService registerConditionEvaluator(
      ConditionDTO.ConditionTypes conditionType,
      ConditionEvaluator evaluator) {
    evaluatorMap.put(conditionType, evaluator);

    return this;
  }

}
