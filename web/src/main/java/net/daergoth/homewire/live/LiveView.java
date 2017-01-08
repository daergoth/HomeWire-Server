package net.daergoth.homewire.live;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import net.daergoth.homewire.BaseUI;
import net.daergoth.homewire.live.component.CustomChartFactory;
import net.daergoth.homewire.live.component.CustomChartRepository;
import net.daergoth.homewire.live.component.RefreshableChart;
import net.daergoth.homewire.setup.SensorSetupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

@SpringView(name = LiveView.VIEW_NAME, ui = BaseUI.class)
@Title("Live - HomeWire")
public class LiveView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "live";

  private CssLayout dashboard;

  private Map<String, RefreshableChart> gaugeMap;

  @Autowired
  private LiveSensorDataService liveSensorDataService;

  @Autowired
  private CustomChartRepository customChartRepository;

  @Autowired
  private SensorSetupService sensorSetupService;

  @Autowired
  private BaseUI ui;

  @PostConstruct
  void init() {
    Label header = new Label("Live");
    header.setStyleName(ValoTheme.LABEL_H1);
    addComponent(header);

    dashboard = new CssLayout();
    dashboard.setSizeFull();

    gaugeMap = new HashMap<>();

    generateDashboard();

    addComponent(dashboard);

    ui.setPollInterval(2000);
    ui.addPollListener(event -> {
      generateDashboard();
    });
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {
    // This view is constructed in the init() method()
  }

  private void generateDashboard() {
    for (LiveDataDTO liveData : liveSensorDataService.getCurrentSensorData()) {

      if (gaugeMap.containsKey(liveData.getType() + liveData.getId())) {
        RefreshableChart refreshableChart = gaugeMap.get(liveData.getType() + liveData.getId());

        if (liveData.getValue().getClass().equals(refreshableChart.getRefreshType())) {
          refreshableChart.refresh(liveData.getValue());
        }

      } else {
        RefreshableChart refreshableChart = getGauge(liveData);

        gaugeMap.put(liveData.getType() + liveData.getId(), refreshableChart);

        dashboard.addComponent(refreshableChart);
      }
    }
  }

  private RefreshableChart getGauge(LiveDataDTO liveDataDTO) {
    CustomChartFactory chartFactory = customChartRepository.getChartFactory(liveDataDTO.getType());

    if (chartFactory != null) {
      RefreshableChart refreshableChart =
          chartFactory.createChart(sensorSetupService
              .getSensorNameByIdAndType(liveDataDTO.getId(), liveDataDTO.getType()));

      if (liveDataDTO.getValue().getClass().equals(refreshableChart.getRefreshType())) {
        refreshableChart.refresh(liveDataDTO.getValue());
      }

      return refreshableChart;
    } else {
      return new RefreshableChart() {
        @Override
        public void refresh(Object value) {

        }

        @Override
        public Class getRefreshType() {
          return null;
        }
      };
    }

  }
}