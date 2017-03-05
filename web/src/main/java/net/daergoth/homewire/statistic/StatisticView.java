package net.daergoth.homewire.statistic;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import net.daergoth.homewire.setup.SensorSetupService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringView(name = StatisticView.VIEW_NAME)
@Title("Statistics - HomeWire")
public class StatisticView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "stats";

  private List<TimeSeriesChart> charts;

  @Autowired
  private StatisticSensorDataService statisticSensorDataService;

  @Autowired
  private SensorSetupService sensorSetupService;

  @PostConstruct
  void init() {
    this.charts = new LinkedList<>();

    Label header = new Label("Statistics");
    header.setStyleName(ValoTheme.LABEL_H1);

    addComponent(header);

    CssLayout contentLayout = new CssLayout();
    contentLayout.setWidth("100%");

    contentLayout.addComponent(createSensorList());

    contentLayout.addComponent(createStatAccordion());

    addComponent(contentLayout);

  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {

  }

  private Component createSensorList() {
    VerticalLayout listLayout = new VerticalLayout();
    listLayout.setWidth(25, Unit.PERCENTAGE);

    sensorSetupService.getSensorDtosGroupedByType().forEach((groupName, sensorDTOS) -> {
      VerticalLayout sensorList = new VerticalLayout();

      sensorDTOS.forEach(sensorDTO -> {
        CheckBox c = new CheckBox(sensorDTO.getName());

        c.addValueChangeListener(event -> {
          updateChart(sensorDTO.getType() + sensorDTO.getDevId(),
              (Boolean) event.getProperty().getValue());
        });

        sensorList.addComponent(c);
      });

      Panel typePanel = new Panel(groupName.substring(0, 1).toUpperCase() + groupName.substring(1));
      typePanel.setContent(sensorList);
      listLayout.addComponent(typePanel);
    });

    return listLayout;
  }

  private Component createStatAccordion() {
    Accordion statAccordion = new Accordion();
    statAccordion.setWidth(75, Unit.PERCENTAGE);

    Collection<TimeSeries> minuteGroupedData =
        groupDataById(statisticSensorDataService.getStats(SensorMeasurementEntity.MeasurementInterval.MINUTE)).values();
    TimeSeriesChart minuteChart = new TimeSeriesChart(minuteGroupedData);
    charts.add(minuteChart);
    statAccordion.addTab(minuteChart, "By minute");

    Collection<TimeSeries> hourGroupedData =
        groupDataById(statisticSensorDataService.getStats(SensorMeasurementEntity.MeasurementInterval.HOUR)).values();
    TimeSeriesChart hourChart = new TimeSeriesChart(hourGroupedData);
    charts.add(hourChart);
    statAccordion.addTab(hourChart, "By hour");

    Collection<TimeSeries> dayGroupedData =
        groupDataById(statisticSensorDataService.getStats(SensorMeasurementEntity.MeasurementInterval.HOUR)).values();
    TimeSeriesChart dayChart = new TimeSeriesChart(dayGroupedData);
    charts.add(dayChart);
    statAccordion.addTab(dayChart, "By day");

    return statAccordion;
  }

  private HashMap<String, TimeSeries> groupDataById(List<StatisticDataDTO> dataList) {
    HashMap<String, TimeSeries> dataMap = new HashMap<>();
    dataList.forEach(dto -> {
      TimeSeries series = dataMap.computeIfAbsent(dto.getType() + dto.getId(),
          id -> new TimeSeries(
              dto.getType() + dto.getId(),
              sensorSetupService.getSensorNameByIdAndType(dto.getId(), dto.getType()),
              dto.getType(),
              new LinkedList<>()));

      series.addData(
          new TimeSeries.TimeSeriesData(
              Date.from(dto.getTime().toInstant()),
              dto.getValue())
      );
    });
    return dataMap;
  }

  private void updateChart(String id, boolean isVisible) {
    charts.forEach(chart -> chart.setSeriesVisibility(id, isVisible));
  }

}
