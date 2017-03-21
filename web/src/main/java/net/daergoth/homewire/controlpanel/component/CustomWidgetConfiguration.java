package net.daergoth.homewire.controlpanel.component;

import net.daergoth.homewire.NetworkServer;
import net.daergoth.homewire.controlpanel.component.humidity.HumidityWidgetFactory;
import net.daergoth.homewire.controlpanel.component.motion.MotionWidgetFactory;
import net.daergoth.homewire.controlpanel.component.relay.RelayWidgetFactory;
import net.daergoth.homewire.controlpanel.component.soilmoisture.SoilMoistureWidgetFactory;
import net.daergoth.homewire.controlpanel.component.temperature.TemperatureWidgetFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomWidgetConfiguration {

  @Autowired
  private NetworkServer networkServer;

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
        registerWidgetFactory(RelayWidgetFactory.CHART_TYPE, new RelayWidgetFactory(networkServer));

    return customWidgetRepository;
  }

}
