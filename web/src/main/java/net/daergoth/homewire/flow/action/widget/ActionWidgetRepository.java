package net.daergoth.homewire.flow.action.widget;

import net.daergoth.homewire.flow.TypeViewDTO;

import java.util.HashMap;
import java.util.Map;

public class ActionWidgetRepository {

  private Map<TypeViewDTO.ActionTypes, ActionWidgetFactory> widgetFactoryMap;

  public ActionWidgetRepository() {
    this.widgetFactoryMap = new HashMap<>();
  }

  public ActionWidgetRepository addFactory(TypeViewDTO.ActionTypes actionTypes,
                                           ActionWidgetFactory actionWidgetFactory) {
    widgetFactoryMap.put(actionTypes, actionWidgetFactory);
    return this;
  }

  public ActionWidgetFactory getFactory(TypeViewDTO.ActionTypes actionTypes) {
    return widgetFactoryMap.get(actionTypes);
  }

}
