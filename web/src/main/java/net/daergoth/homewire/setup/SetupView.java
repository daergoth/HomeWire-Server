package net.daergoth.homewire.setup;


import com.vaadin.annotations.Title;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;
import net.daergoth.homewire.controlpanel.LiveDataService;
import net.daergoth.homewire.flow.persistence.ActionDTO;
import net.daergoth.homewire.flow.persistence.ConditionDTO;
import net.daergoth.homewire.flow.persistence.FlowService;
import net.daergoth.homewire.statistic.StatisticDeviceDataService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import javax.annotation.PostConstruct;

@SpringView(name = SetupView.VIEW_NAME)
@Title("Setup - HomeWire")
public class SetupView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "setup";

  private Grid mainGrid;

  @Autowired
  private StatisticDeviceDataService statisticDeviceDataService;

  @Autowired
  private LiveDataService liveDataService;

  @Autowired
  private FlowService flowService;

  @Autowired
  private DeviceSetupService deviceSetupService;

  @PostConstruct
  void init() {
    Label header = new Label("Setup");
    header.setStyleName(ValoTheme.LABEL_H1);

    addComponent(header);

    // Generate button caption column
    GeneratedPropertyContainer gpc =
        new GeneratedPropertyContainer(
            new BeanItemContainer<>(DeviceDTO.class, deviceSetupService.getAllDeviceDtos()));

    gpc.addGeneratedProperty("delete",
        new PropertyValueGenerator<String>() {

          @Override
          public String getValue(Item item, Object itemId,
                                 Object propertyId) {
            return "Delete"; // The caption
          }

          @Override
          public Class<String> getType() {
            return String.class;
          }
        });

    mainGrid = new Grid(gpc);
    mainGrid.setEditorEnabled(true);
    mainGrid.setWidth(90, Unit.PERCENTAGE);

    mainGrid.getColumn("delete")
        .setRenderer(new ButtonRenderer(e -> { // Java 8
          DeviceDTO clickedDevice = (DeviceDTO) e.getItemId();
          mainGrid.getContainerDataSource().removeItem(clickedDevice);
          deviceSetupService
              .removeDeviceDtoByDevIdAndDevType(clickedDevice.getDevId(), clickedDevice.getType());
          getUI().addWindow(getDeleteConfirmWindow(clickedDevice));
        }));

    mainGrid.getColumn("devId").setEditable(false);
    mainGrid.getColumn("category").setEditable(false);
    mainGrid.getColumn("type").setEditable(false);
    mainGrid.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
      @Override
      public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
      }

      @Override
      public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
        Short devId = (Short) commitEvent.getFieldBinder().getItemDataSource()
            .getItemProperty("devId").getValue();
        String type = (String) commitEvent.getFieldBinder().getItemDataSource()
            .getItemProperty("type").getValue();

        DeviceDTO toUpdate = deviceSetupService.getDeviceDtoByIdAndType(devId, type);
        toUpdate.setName((String) commitEvent.getFieldBinder().getField("name").getValue());
        toUpdate
            .setTrusted((Boolean) commitEvent.getFieldBinder().getField("trusted").getValue());

        deviceSetupService.updateDeviceDto(toUpdate);
      }
    });

    addComponent(mainGrid);

  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {

  }

  private Window getDeleteConfirmWindow(DeviceDTO deviceDTO) {
    final String[] exportFileName = {""};

    Window confirmWindow = new Window("Delete");
    confirmWindow.setResizable(false);
    confirmWindow.setModal(true);
    confirmWindow.setDraggable(false);
    confirmWindow.setClosable(true);
    confirmWindow.addCloseListener(e -> {
      if (!exportFileName[0].isEmpty()) {
        new File(exportFileName[0]).delete();
      }
    });

    Label areYouSureLabel = new Label("Are you sure you want to delete the device?");

    Label ifWantExport = new Label("If you wish, you can export all recorded data for the device.");

    VerticalLayout contentLayout = new VerticalLayout(areYouSureLabel, ifWantExport);

    Button saveAndDeleteButton = new Button("Export and Delete", event -> {
      exportFileName[0] =
          statisticDeviceDataService
              .exportDataToCsv(deviceDTO.getDevId(), deviceDTO.getType(), deviceDTO.getName());

      if (!exportFileName[0].isEmpty()) {
        contentLayout.addComponent(new Link("Download export, click before exiting!",
            new FileResource(new File(exportFileName[0]))));
        deleteDevice(deviceDTO);
      } else {
        contentLayout.addComponent(
            new Label("Export failed, please try again! The device has not been deleted."));
      }
    });

    Button deleteOnly = new Button("Delete only", event -> {
      deleteDevice(deviceDTO);
      confirmWindow.close();
    });

    confirmWindow.setContent(
        new VerticalLayout(contentLayout, new HorizontalLayout(saveAndDeleteButton, deleteOnly)));

    return confirmWindow;
  }

  private void deleteDevice(DeviceDTO deviceDTO) {
    mainGrid.getContainerDataSource().removeItem(deviceDTO);
    deviceSetupService
        .removeDeviceDtoByDevIdAndDevType(deviceDTO.getDevId(), deviceDTO.getType());
    statisticDeviceDataService
        .removeStatsForDevIdAndDevType(deviceDTO.getDevId(), deviceDTO.getType());
    liveDataService
        .removeCurrentDeviceDataForDevIdAndDevType(deviceDTO.getDevId(), deviceDTO.getType());

    flowService.getAllFlowDtos().forEach(flowDTO -> {
      for (ConditionDTO conditionDTO : flowDTO.getConditionList()) {
        if (conditionDTO.getDevId().equals(deviceDTO.getDevId()) &&
            conditionDTO.getDevType().equals(deviceDTO.getType())) {
          flowService.removeFlowDto(flowDTO.getId());
          break;
        }
      }

      for (ActionDTO actionDTO : flowDTO.getActionList()) {
        if (actionDTO.getDevId().equals(deviceDTO.getDevId()) &&
            actionDTO.getDevType().equals(deviceDTO.getType())) {
          flowService.removeFlowDto(flowDTO.getId());

          break;
        }
      }
    });
  }
}
