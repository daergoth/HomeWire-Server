package net.daergoth.homewire.live.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomChartConfiguration {

  @Bean
  public CustomChartRepository customChartRepository() {
    CustomChartRepository customChartRepository = new CustomChartRepository();

    customChartRepository
        .registerChartFactory(TemperatureChartFactory.CHART_TYPE, new TemperatureChartFactory());
    customChartRepository
        .registerChartFactory(HumidityChartFactory.CHART_TYPE, new HumidityChartFactory());
    customChartRepository.
        registerChartFactory(SoilMoistureChartFactory.CHART_TYPE, new SoilMoistureChartFactory());

    return customChartRepository;
  }

}
