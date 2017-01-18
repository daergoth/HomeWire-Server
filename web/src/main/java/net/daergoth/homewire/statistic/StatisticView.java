package net.daergoth.homewire.statistic;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import net.daergoth.homewire.setup.SensorDTO;
import net.daergoth.homewire.setup.SensorSetupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;

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

    HashMap<String, List<SensorDTO>> sensorsGroupByType = new HashMap<>();

    sensorSetupService.getAllSensorDtos().forEach(dto -> {
      List<SensorDTO> dtoList =
          sensorsGroupByType.computeIfAbsent(dto.getType(), s -> new LinkedList<>());

      dtoList.add(dto);
    });

    sensorsGroupByType.forEach((s, sensorDTOS) -> {
      VerticalLayout sensorList = new VerticalLayout();
      sensorDTOS.forEach(sensorDTO -> {
        CheckBox c = new CheckBox(sensorDTO.getName());
        c.addValueChangeListener(event -> {
          updateCharts(sensorDTO.getType() + sensorDTO.getDevId(),
              (Boolean) event.getProperty().getValue());
        });

        sensorList.addComponent(c);
      });

      Panel typePanel = new Panel(s.substring(0, 1).toUpperCase() + s.substring(1));
      typePanel.setContent(sensorList);
      listLayout.addComponent(typePanel);
    });

    return listLayout;
  }

  private Component createStatAccordion() {
    Accordion statAccordion = new Accordion();
    statAccordion.setWidth(75, Unit.PERCENTAGE);

    Collection<TimeSeries> minuteGroupedData =
        groupDataById(statisticSensorDataService.getStatByMinute()).values();
    TimeSeriesChart minuteChart = new TimeSeriesChart(minuteGroupedData);
    charts.add(minuteChart);
    statAccordion.addTab(minuteChart, "By minute");

    Collection<TimeSeries> hourGroupedData =
        groupDataById(statisticSensorDataService.getStatByHour()).values();
    TimeSeriesChart hourChart = new TimeSeriesChart(hourGroupedData);
    charts.add(hourChart);
    statAccordion.addTab(hourChart, "By hour");

    Collection<TimeSeries> dayGroupedData =
        groupDataById(statisticSensorDataService.getStatByDay()).values();
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

  private void updateCharts(String id, boolean isVisible) {
    charts.forEach(chart -> chart.setSeriesVisibility(id, isVisible));
  }

}
