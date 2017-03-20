package net.daergoth.homewire.live.component;

import net.daergoth.homewire.live.component.humidity.HumidityWidgetFactory;
import net.daergoth.homewire.live.component.motion.MotionWidgetFactory;
import net.daergoth.homewire.live.component.relay.RelayWidgetFactory;
import net.daergoth.homewire.live.component.soilmoisture.SoilMoistureWidgetFactory;
import net.daergoth.homewire.live.component.temperature.TemperatureWidgetFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomWidgetConfiguration {

  @Bean
  public CustomWidgetRepository customChartRepository() {
    CustomWidgetRepository customWidgetRepository = new CustomWidgetRepository();

    customWidgetRepository
        .registerWidgetFactory(TemperatureWidgetFactory.CHART_TYPE, new TemperatureWidgetFactory());
    customWidgetRepository
        .registerWidgetFactory(HumidityWidgetFactory.CHART_TYPE, new HumidityWidgetFactory());
    customWidgetRepository.
        registerWidgetFactory(SoilMoistureWidgetFactory.CHART_TYPE,
            new SoilMoistureWidgetFactory());
    customWidgetRepository.
        registerWidgetFactory(MotionWidgetFactory.CHART_TYPE, new MotionWidgetFactory());
    customWidgetRepository.
        registerWidgetFactory(RelayWidgetFactory.CHART_TYPE, new RelayWidgetFactory());

    return customWidgetRepository;
  }

}
