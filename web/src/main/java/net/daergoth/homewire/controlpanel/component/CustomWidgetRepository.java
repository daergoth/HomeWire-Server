package net.daergoth.homewire.controlpanel.component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomWidgetRepository {

  private Map<String, CustomWidgetFactory> factoryMap;

  public CustomWidgetRepository() {
    this.factoryMap = new ConcurrentHashMap<>();
  }

  public void registerWidgetFactory(String type, CustomWidgetFactory customWidgetFactory) {
    factoryMap.putIfAbsent(type, customWidgetFactory);
  }

  public CustomWidgetFactory getWidgetFactory(String type) {
    return factoryMap.get(type);
  }

}
