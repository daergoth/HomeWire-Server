package net.daergoth.homewire.flow.condition.widget;

import net.daergoth.homewire.flow.ConditionDTO;

public interface ConditionWidgetFactory {

  ConditionalWidget createWidget(ConditionDTO conditionDTO);

}
