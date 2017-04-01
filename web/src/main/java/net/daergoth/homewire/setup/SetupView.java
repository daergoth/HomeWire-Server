package net.daergoth.homewire.setup;


import com.vaadin.annotations.Title;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = SetupView.VIEW_NAME)
@Title("Setup - HomeWire")
public class SetupView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "setup";

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

    Grid grid = new Grid(gpc);

    grid.getColumn("delete")
        .setRenderer(new ButtonRenderer(e -> { // Java 8
          DeviceDTO clickedDevice = (DeviceDTO) e.getItemId();
          grid.getContainerDataSource().removeItem(clickedDevice);
          deviceSetupService
              .removeDeviceDtoByDevIdAndDevType(clickedDevice.getDevId(), clickedDevice.getType());
        }));

    grid.setWidth("90%");
    grid.setEditorEnabled(true);
    grid.getColumn("devId").setEditable(false);
    grid.getColumn("category").setEditable(false);
    grid.getColumn("type").setEditable(false);
    grid.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
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

    addComponent(grid);

  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {

  }
}
