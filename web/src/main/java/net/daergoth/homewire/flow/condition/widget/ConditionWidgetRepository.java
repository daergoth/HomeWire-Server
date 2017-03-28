package net.daergoth.homewire.flow.condition.widget;

import net.daergoth.homewire.flow.TypeViewDTO;

import java.util.HashMap;
import java.util.Map;

public class ConditionWidgetRepository {

  private Map<TypeViewDTO.ConditionTypes, ConditionWidgetFactory> widgetFactoryMap;

  public ConditionWidgetRepository() {
    this.widgetFactoryMap = new HashMap<>();
  }

  public ConditionWidgetRepository addFactory(TypeViewDTO.ConditionTypes conditionType,
                                              ConditionWidgetFactory conditionWidgetFactory) {
    widgetFactoryMap.put(conditionType, conditionWidgetFactory);
    return this;
  }

  public ConditionWidgetFactory getFactory(TypeViewDTO.ConditionTypes conditionType) {
    return widgetFactoryMap.get(conditionType);
  }

}
