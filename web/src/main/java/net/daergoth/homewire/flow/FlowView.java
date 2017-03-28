package net.daergoth.homewire.flow;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import net.daergoth.homewire.setup.DeviceSetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.stackpanel.StackPanel;
import org.vaadin.ui.NumberField;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringView(name = FlowView.VIEW_NAME)
@Title("Flows - HomeWire")
public class FlowView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "flows";

  @Autowired
  private FlowService flowService;

  @Autowired
  private DeviceSetupService deviceSetupService;

  @PostConstruct
  void init() {
    Label header = new Label("Flows");
    header.setStyleName(ValoTheme.LABEL_H1);

    addComponent(header);

    Button testButton = new Button("Save test");
    testButton.addClickListener(event -> {
      FlowDTO flowDTO = new FlowDTO();
      flowDTO.setId(1);
      flowDTO.setName("Test1");
      flowDTO.setOrderNum(2);
      flowDTO.addCondition(new ConditionDTO((short) 4, "temperature", "greaterthan", "23"));
      flowDTO.addAction(new ActionDTO((short) 2, "relay", "set", "0"));
      flowDTO.addAction(new ActionDTO((short) 3, "relay", "set", "0"));

      flowService.saveFlowDto(flowDTO);
    });
    addComponent(testButton);

    List<FlowDTO> flows = flowService.getAllFlowDtos();

    flows.forEach(flowDTO -> {
      FlowViewPanel flowViewPanel = new FlowViewPanel(flowDTO, deviceSetupService);
      flowViewPanel.setSaveConsumer(dto -> flowService.saveFlowDto(dto));
      addComponent(flowViewPanel);
    });
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    // This view is constructed in the init() method()
  }

  private Panel getPanelForFlow(FlowDTO flowDTO) {
    Panel panel = new Panel(flowDTO.getName());
    panel.setWidth(100, Unit.PERCENTAGE);

    StackPanel stackPanel = StackPanel.extend(panel);
    stackPanel.setToggleIconsEnabled(false);

    GridLayout gridLayout = new GridLayout(2, 3);
    gridLayout.setWidth(100, Unit.PERCENTAGE);

    Label nameLabel = new Label("Name:");
    gridLayout.addComponent(nameLabel, 0, 0);

    TextField nameTextField = new TextField();
    nameTextField.setValue(flowDTO.getName());
    nameTextField.addValueChangeListener(event -> {
      flowDTO.setName((String) event.getProperty().getValue());

      flowService.saveFlowDto(flowDTO);
    });
    gridLayout.addComponent(nameTextField, 1, 0);

    Label orderNumLabel = new Label("Order number:");
    gridLayout.addComponent(orderNumLabel, 0, 1);

    NumberField orderNumField = new NumberField();
    orderNumField.setValidationVisible(false);
    orderNumField.setDecimalAllowed(false);
    orderNumField.setMinValue(0);
    orderNumField.setValue(Double.valueOf(flowDTO.getOrderNum()));
    orderNumField.addValueChangeListener(event -> {
      Integer newOrderNum = Integer.parseInt((String) event.getProperty().getValue());

      flowDTO.setOrderNum(newOrderNum);

      flowService.saveFlowDto(flowDTO);
    });
    gridLayout.addComponent(orderNumField, 1, 1);

    VerticalLayout conditionsVerticalLayout = new VerticalLayout(new Label("Conditions"));
    flowDTO.getConditionList().forEach(conditionDTO -> {
      ComboBox deviceComboBox = new ComboBox("Device", deviceSetupService.getAllDeviceDtos());
      deviceComboBox.setValue(deviceSetupService
          .getDeviceDtoByIdAndType(conditionDTO.getDevId(), conditionDTO.getDevType()));


      TextField type = new TextField("Type", conditionDTO.getType());
      TextField param = new TextField("Parameter", conditionDTO.getParameter());
      HorizontalLayout
          conditionLayout =
          new HorizontalLayout(deviceComboBox, type, param);
      conditionsVerticalLayout.addComponent(conditionLayout);
    });
    gridLayout.addComponent(conditionsVerticalLayout, 0, 2);

    VerticalLayout actionsVerticalLayout = new VerticalLayout(new Label("Actions"));
    flowDTO.getActionList().forEach(actionDTO -> {
      TextField devId = new TextField("Device ID", String.valueOf(actionDTO.getDevId()));
      TextField devType = new TextField("Device type", actionDTO.getDevType());
      TextField type = new TextField("Type", actionDTO.getType());
      TextField param = new TextField("Parameter", actionDTO.getParameter());
      HorizontalLayout actionLayout = new HorizontalLayout(devId, devType, type, param);
      actionsVerticalLayout.addComponent(actionLayout);
    });
    gridLayout.addComponent(actionsVerticalLayout, 1, 2);

    panel.setContent(gridLayout);

    return panel;
  }

}
