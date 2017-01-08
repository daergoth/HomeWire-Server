package net.daergoth.homewire.live.component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomChartRepository {

  private Map<String, CustomChartFactory> factoryMap;

  public CustomChartRepository() {
    this.factoryMap = new ConcurrentHashMap<>();
  }

  public void registerChartFactory(String type, CustomChartFactory customChartFactory) {
    factoryMap.putIfAbsent(type, customChartFactory);
  }

  public CustomChartFactory getChartFactory(String type) {
    return factoryMap.get(type);
  }

}
