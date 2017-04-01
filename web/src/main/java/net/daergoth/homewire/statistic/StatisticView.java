package net.daergoth.homewire.statistic;

import com.vaadin.annotations.Title;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import net.daergoth.homewire.flow.DeviceViewDTO;
import net.daergoth.homewire.setup.DeviceSetupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

@SpringView(name = StatisticView.VIEW_NAME)
@Title("Statistics - HomeWire")
public class StatisticView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "stats";

  private List<TimeSeriesChart> charts;

  @Autowired
  private StatisticDeviceDataService statisticDeviceDataService;

  @Autowired
  private DeviceSetupService deviceSetupService;

  @PostConstruct
  void init() {
    this.charts = new LinkedList<>();

    Label header = new Label("Statistics");
    header.setStyleName(ValoTheme.LABEL_H1);

    addComponent(header);

    CssLayout contentLayout = new CssLayout();
    contentLayout.setWidth("100%");

    VerticalLayout sidebarLayout = new VerticalLayout();
    sidebarLayout.setWidth(25, Unit.PERCENTAGE);

    Button exportPopupButton = new Button("Export data", event -> {
      getUI().addWindow(getExportWindow());
    });
    sidebarLayout.addComponent(exportPopupButton);

    sidebarLayout.addComponent(createSensorList());

    contentLayout.addComponent(sidebarLayout);

    contentLayout.addComponent(createStatAccordion());

    addComponent(contentLayout);

  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {

  }

  private Component createSensorList() {
    VerticalLayout listLayout = new VerticalLayout();
    //listLayout.setWidth(25, Unit.PERCENTAGE);

    deviceSetupService.getSensorDtosGroupedByType().forEach((groupName, sensorDTOS) -> {
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
        groupDataById(statisticDeviceDataService.getStats(DeviceStateEntity.StateInterval.MINUTE))
            .values();
    TimeSeriesChart minuteChart = new TimeSeriesChart(minuteGroupedData);
    charts.add(minuteChart);
    statAccordion.addTab(minuteChart, "By minute");

    Collection<TimeSeries> hourGroupedData =
        groupDataById(statisticDeviceDataService.getStats(DeviceStateEntity.StateInterval.HOUR))
            .values();
    TimeSeriesChart hourChart = new TimeSeriesChart(hourGroupedData);
    charts.add(hourChart);
    statAccordion.addTab(hourChart, "By hour");

    Collection<TimeSeries> dayGroupedData =
        groupDataById(statisticDeviceDataService.getStats(DeviceStateEntity.StateInterval.HOUR))
            .values();
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
              deviceSetupService.getDeviceNameByIdAndType(dto.getId(), dto.getType()),
              dto.getType()));

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

  private Window getExportWindow() {
    final String[] fileName = {""};

    Window selectWindow = new Window("Export data");
    selectWindow.setClosable(true);
    selectWindow.setDraggable(false);
    selectWindow.setModal(true);
    selectWindow.setResizable(false);
    selectWindow.setWidth(300, Unit.PIXELS);
    selectWindow.addCloseListener(e -> {
      if (!fileName[0].isEmpty()) {
        new File(fileName[0]).delete();
      }
    });

    VerticalLayout windowRoot = new VerticalLayout();

    ComboBox devicesComboBox = new ComboBox("Devices");
    devicesComboBox.setWidth(100, Unit.PERCENTAGE);
    devicesComboBox.setNullSelectionItemId(false);
    devicesComboBox.setContainerDataSource(
        new BeanItemContainer<>(
            DeviceViewDTO.class,
            deviceSetupService.getAllDeviceDtos().stream()
                .map(deviceDTO ->
                    new DeviceViewDTO(deviceDTO.getDevId(), deviceDTO.getType(),
                        deviceDTO.getName()))
                .collect(Collectors.toList())
        )
    );
    windowRoot.addComponent(devicesComboBox);

    Button toExportButton = new Button("Export", event -> {
      DeviceViewDTO selectedDto = (DeviceViewDTO) devicesComboBox.getValue();
      String createdFilePath =
          statisticDeviceDataService.exportDataToCsv(selectedDto.getDevId(), selectedDto.getType(),
              selectedDto.getName());

      if (!createdFilePath.isEmpty()) {
        fileName[0] = createdFilePath;
        windowRoot
            .addComponent(new Link("Download CSV", new FileResource(new File(createdFilePath))));
      } else {
        windowRoot.addComponent(new Label("Export failed! Please Try again!"));
      }

    });
    windowRoot.addComponent(toExportButton);

    selectWindow.setContent(windowRoot);

    return selectWindow;
  }

}
