package net.daergoth.homewire.statistic;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import net.daergoth.homewire.setup.SensorSetupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import javax.annotation.PostConstruct;

@SpringView(name = StatisticView.VIEW_NAME)
@Title("Statistics - HomeWire")
public class StatisticView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "stats";

  @Autowired
  private StatisticSensorDataService statisticSensorDataService;

  @Autowired
  private SensorSetupService sensorSetupService;

  @PostConstruct
  void init() {
    Label header = new Label("Statistics");
    header.setStyleName(ValoTheme.LABEL_H1);

    addComponent(header);

    Label min = new Label("By minute");
    min.setStyleName(ValoTheme.LABEL_H2);
    addComponent(min);

    HashMap<Short, TimeSeries> seriesMap = new HashMap<>();
    statisticSensorDataService.getStatByMinute("temperature").forEach(dto -> {
      TimeSeries series = seriesMap.computeIfAbsent(dto.getId(),
          id -> new TimeSeries(
              sensorSetupService.getSensorNameByIdAndType(dto.getId(), dto.getType()),
              new LinkedList<TimeSeries.TimeSeriesData>()));

      series.addData(
          new TimeSeries.TimeSeriesData(
              Date.from(dto.getTime().toInstant()),
              dto.getValue())
      );

      seriesMap.put(dto.getId(), series);
    });

    TimeSeriesChart timeSeriesChart = new TimeSeriesChart(seriesMap.values());
    addComponent(timeSeriesChart);

    Label hour = new Label("By hour");
    hour.setStyleName(ValoTheme.LABEL_H2);
    addComponent(hour);

    statisticSensorDataService.getStatByHour("temperature").forEach(dto -> {
      addComponent(new Label(
          "[" + dto.getTime() + "] " + sensorSetupService
              .getSensorNameByIdAndType(dto.getId(), dto.getType()) + ": " + dto.getValue()));
    });


  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {

  }

}
