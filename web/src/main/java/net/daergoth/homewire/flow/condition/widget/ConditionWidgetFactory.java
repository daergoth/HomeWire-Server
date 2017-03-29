package net.daergoth.homewire.flow.condition.widget;

import net.daergoth.homewire.flow.ConditionDTO;

public interface ConditionWidgetFactory {

  ConditionWidget createWidget(ConditionDTO conditionDTO);

}
