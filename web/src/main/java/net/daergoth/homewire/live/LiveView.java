package net.daergoth.homewire.live;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.justgage.JustGage;
import org.vaadin.justgage.JustGageConfiguration;

import javax.annotation.PostConstruct;

@SpringView(name = LiveView.VIEW_NAME)
public class LiveView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "live";

  @Autowired
  private LiveSensorDataService liveSensorDataService;

  @PostConstruct
  void init() {
    Label header = new Label("Live");
    header.setStyleName(ValoTheme.LABEL_H1);
    addComponent(header);

    CssLayout dashboard = new CssLayout();



    for (LiveDataDTO liveDataDTO : liveSensorDataService.getCurrentSensorData()) {
      dashboard.addComponent(getGauge(liveDataDTO));
    }

  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {
    // This view is constructed in the init() method()
  }

  private Component getGauge(LiveDataDTO liveDataDTO) {
    if (liveDataDTO.getType().equals("temperature")) {
      JustGageConfiguration gageConfiguration = new JustGageConfiguration();
      gageConfiguration.min = -10f;
      gageConfiguration.max = 45f;
      gageConfiguration.title = "Temperature - " + liveDataDTO.getId();
      gageConfiguration.symbol = "°C";
      gageConfiguration.value = liveDataDTO.getValue();

      return new JustGage(gageConfiguration);
    } else if (liveDataDTO.getType().equals("humidity")) {
      JustGageConfiguration gageConfiguration = new JustGageConfiguration();
      gageConfiguration.min = 0f;
      gageConfiguration.max = 100f;
      gageConfiguration.title = "Humidity - " + liveDataDTO.getId();
      gageConfiguration.symbol = "%";
      gageConfiguration.value = liveDataDTO.getValue();

      return new JustGage(gageConfiguration);
    } else {
      return new Label("Invalid sensor data!");
    }
  }
}