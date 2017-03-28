package net.daergoth.homewire.flow.action.widget;

import net.daergoth.homewire.flow.ActionDTO;

public interface ActionWidgetFactory {

  ActionWidget createWidget(ActionDTO conditionDTO);

}
