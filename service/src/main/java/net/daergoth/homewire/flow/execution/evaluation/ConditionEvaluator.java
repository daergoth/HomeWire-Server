package net.daergoth.homewire.flow.execution.evaluation;

import net.daergoth.homewire.flow.persistence.ConditionDTO;

public interface ConditionEvaluator {

  boolean evaluateCondition(ConditionDTO conditionDTO);

}
