package net.daergoth.homewire.flow;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import net.daergoth.homewire.flow.action.ActionPanel;
import net.daergoth.homewire.flow.action.widget.ActionWidgetRepository;
import net.daergoth.homewire.flow.condition.ConditionPanel;
import net.daergoth.homewire.flow.condition.widget.ConditionWidgetRepository;
import net.daergoth.homewire.setup.DeviceSetupService;
import org.modelmapper.ModelMapper;
import org.vaadin.addons.stackpanel.StackPanel;

import java.util.function.Consumer;


public class FlowViewPanel extends CustomComponent {

  private Panel innerPanel;

  private final FlowDTO flowDTO;

  private Consumer<FlowDTO> saveConsumer;

  private final DeviceSetupService deviceSetupService;

  private final ModelMapper modelMapper;

  private final ConditionWidgetRepository conditionWidgetRepository;

  private final ActionWidgetRepository actionWidgetRepository;

  public FlowViewPanel(FlowDTO flowDTO, DeviceSetupService deviceSetupService,
                       ModelMapper modelMapper,
                       ConditionWidgetRepository conditionWidgetRepository,
                       ActionWidgetRepository actionWidgetRepository) {
    this.flowDTO = flowDTO;
    this.innerPanel = new Panel(flowDTO.getName());
    this.deviceSetupService = deviceSetupService;
    this.modelMapper = modelMapper;
    this.conditionWidgetRepository = conditionWidgetRepository;
    this.actionWidgetRepository = actionWidgetRepository;

    StackPanel.extend(innerPanel).setToggleIconsEnabled(false);

    innerPanel.setContent(getPanelGrid());

    setCompositionRoot(innerPanel);
  }

  private GridLayout getPanelGrid() {
    GridLayout panelGrid = new GridLayout(2, 4);
    panelGrid.setWidth(100, Unit.PERCENTAGE);

    // ROW 1
    // Name TextField
    TextField nameTextField = new TextField("Name", flowDTO.getName());
    nameTextField.addValueChangeListener(event -> {
      if (event.getProperty().getType() == String.class) {
        String newName = (String) event.getProperty().getValue();

        flowDTO.setName(newName);
        innerPanel.setCaption(newName);

        saveIfPossible();
      }
    });
    panelGrid.addComponent(nameTextField, 0, 0);

    // ROW 2
    // Order number
    Label orderTitleLabel = new Label("Order number: ");
    Label orderNumLabel = new Label(String.valueOf(flowDTO.getOrderNum()));
    Button orderNumUpButton = new Button("^", event -> {
      int newOrderNum = flowDTO.getOrderNum() + 1;

      orderNumLabel.setValue(String.valueOf(newOrderNum));
      flowDTO.setOrderNum(newOrderNum);

      saveIfPossible();
    });
    Button orderNumDownButton = new Button("˘", event -> {
      int newOrderNum = flowDTO.getOrderNum() - 1;

      if (newOrderNum < 0) {
        newOrderNum = 0;
      }

      orderNumLabel.setValue(String.valueOf(newOrderNum));
      flowDTO.setOrderNum(newOrderNum);

      saveIfPossible();
    });
    panelGrid.addComponent(
        new HorizontalLayout(orderTitleLabel, orderNumLabel, orderNumUpButton, orderNumDownButton),
        0, 1);

    // ROW 3
    // Conditions label
    Label conditionsLabel = new Label("Conditions");
    conditionsLabel.setSizeUndefined();
    panelGrid.addComponent(conditionsLabel, 0, 2);

    // Actions label
    panelGrid.addComponent(new Label("Actions"), 1, 2);

    // ROW 4
    // Condition list
    VerticalLayout conditionsVerticalLayout = new VerticalLayout();
    conditionsVerticalLayout.setSizeUndefined();

    flowDTO.getConditionList().forEach(
        conditionDTO -> conditionsVerticalLayout
            .addComponent(new ConditionPanel(conditionDTO, conditionWidgetRepository,
                condDto -> saveIfPossible()
            )));

    /*
    flowDTO.getConditionList().forEach(conditionDTO -> {
      // Devices
      ComboBox devicesComboBox = new ComboBox("Devices");
      devicesComboBox.setNullSelectionItemId(false);
      devicesComboBox.setContainerDataSource(
          new BeanItemContainer<>(
              DeviceViewDTO.class,
              deviceSetupService.getAllDeviceDtos().stream()
                  .map(deviceDTO -> modelMapper.map(deviceDTO, DeviceViewDTO.class))
                  .collect(Collectors.toList())
          )
      );
      devicesComboBox.select(
          modelMapper.map(
              deviceSetupService
                  .getDeviceDtoByIdAndType(conditionDTO.getDevId(), conditionDTO.getDevType()),
              DeviceViewDTO.class
          )
      );
      // Device listing only needed if the condition is comparision type
      devicesComboBox.setVisible(false);
      devicesComboBox.addValueChangeListener(event -> {
        DeviceViewDTO selected = (DeviceViewDTO) event.getProperty().getValue();

        int i = flowDTO.getConditionList().indexOf(conditionDTO);
        flowDTO.removeCondition(conditionDTO);

        conditionDTO.setDevId(selected.getDevId());
        conditionDTO.setDevType(selected.getType());

        flowDTO.addCondition(i, conditionDTO);

        saveIfPossible();
      });
      devicesComboBox
          .setVisible(conditionDTO.getConditionType() == ConditionDTO.ConditionTypes.COMPARISION);

      // Types
      ComboBox typeComboBox = new ComboBox("Type");
      typeComboBox.setNullSelectionAllowed(false);
      typeComboBox.addItems(TypeViewDTO.CONDITION_TYPE_VIEWS);
      typeComboBox.setValue(TypeViewDTO.CONDITION_TYPE_VIEWS
          .stream()
          .filter(
              conditionTypeView -> conditionTypeView.getRawType().equals(conditionDTO.getType()))
          .findFirst()
          .get()
      );
      typeComboBox.addValueChangeListener(event -> {
        TypeViewDTO
            selected =
            (TypeViewDTO) event.getProperty().getValue();

        int i = flowDTO.getConditionList().indexOf(conditionDTO);
        flowDTO.removeCondition(conditionDTO);

        conditionDTO.setType(selected.getRawType());

        if (conditionDTO.getConditionType() == ConditionDTO.ConditionTypes.COMPARISION) {
          devicesComboBox.setVisible(true);
        } else {
          devicesComboBox.setVisible(false);
        }

        flowDTO.addCondition(i, conditionDTO);

        saveIfPossible();
      });

      // Parameter
      TextField parameterTextField = new TextField("Parameter", conditionDTO.getParameter());
      parameterTextField.addValueChangeListener(event -> {

        int i = flowDTO.getConditionList().indexOf(conditionDTO);
        flowDTO.removeCondition(conditionDTO);

        conditionDTO.setParameter((String) event.getProperty().getValue());

        flowDTO.addCondition(i, conditionDTO);

        saveIfPossible();
      });

      HorizontalLayout
          conditionRow =
          new HorizontalLayout(devicesComboBox, typeComboBox, parameterTextField);

      conditionsVerticalLayout.addComponent(conditionRow);
    });
    */

    panelGrid.addComponent(conditionsVerticalLayout, 0, 3);

    // Action list
    VerticalLayout actionsVerticalLayout = new VerticalLayout();
    actionsVerticalLayout.setSizeUndefined();

    flowDTO.getActionList().forEach(actionDTO -> actionsVerticalLayout.addComponent(
        new ActionPanel(actionDTO, actionWidgetRepository, actDto -> saveIfPossible())));
    /*
    flowDTO.getActionList().forEach(actionDTO -> {
      // Devices
      ComboBox devicesComboBox = new ComboBox("Actors");
      devicesComboBox.setNullSelectionItemId(false);
      devicesComboBox.setContainerDataSource(
          new BeanItemContainer<>(
              DeviceViewDTO.class,
              deviceSetupService.getAllActors().stream()
                  .map(deviceDTO -> modelMapper.map(deviceDTO, DeviceViewDTO.class))
                  .collect(Collectors.toList())
          )
      );
      devicesComboBox.select(
          modelMapper.map(
              deviceSetupService
                  .getDeviceDtoByIdAndType(actionDTO.getDevId(), actionDTO.getDevType()),
              DeviceViewDTO.class
          )
      );
      // Device listing only needed if the condition is comparision type
      devicesComboBox.setVisible(false);
      devicesComboBox.addValueChangeListener(event -> {
        DeviceViewDTO selected = (DeviceViewDTO) event.getProperty().getValue();

        int i = flowDTO.getActionList().indexOf(actionDTO);
        flowDTO.removeAction(actionDTO);

        actionDTO.setDevId(selected.getDevId());
        actionDTO.setDevType(selected.getType());

        flowDTO.addAction(i, actionDTO);

        saveIfPossible();
      });
      devicesComboBox.setVisible(actionDTO.getActionType() == ActionDTO.ActionTypes.SET);

      // Types
      ComboBox typeComboBox = new ComboBox("Type");
      typeComboBox.setNullSelectionAllowed(false);
      typeComboBox.addItems(TypeViewDTO.ACTION_TYPE_VIEWS);
      typeComboBox.setValue(TypeViewDTO.ACTION_TYPE_VIEWS
          .stream()
          .filter(actionTypeView -> actionTypeView.getRawType().equals(actionDTO.getType()))
          .findFirst()
          .get()
      );
      typeComboBox.addValueChangeListener(event -> {
        TypeViewDTO
            selected =
            (TypeViewDTO) event.getProperty().getValue();

        int i = flowDTO.getActionList().indexOf(actionDTO);
        flowDTO.removeAction(actionDTO);

        actionDTO.setType(selected.getRawType());

        if (actionDTO.getActionType() == ActionDTO.ActionTypes.SET) {
          devicesComboBox.setVisible(true);
        } else {
          devicesComboBox.setVisible(false);
        }

        flowDTO.addAction(i, actionDTO);

        saveIfPossible();
      });

      // Parameter
      TextField parameterTextField = new TextField("Parameter", actionDTO.getParameter());
      parameterTextField.addValueChangeListener(event -> {

        int i = flowDTO.getActionList().indexOf(actionDTO);
        flowDTO.removeAction(actionDTO);

        actionDTO.setParameter((String) event.getProperty().getValue());

        flowDTO.addAction(i, actionDTO);

        saveIfPossible();
      });

      HorizontalLayout
          actionRow =
          new HorizontalLayout(devicesComboBox, typeComboBox, parameterTextField);

      actionsVerticalLayout.addComponent(actionRow);
    });
    */

    panelGrid.addComponent(actionsVerticalLayout, 1, 3);

    return panelGrid;
  }

  private HorizontalLayout getConditionLayoutFromConditionDto() {
    return new HorizontalLayout();
  }

  public void setSaveConsumer(Consumer<FlowDTO> saveConsumer) {
    this.saveConsumer = saveConsumer;
  }

  private void saveIfPossible() {
    if (saveConsumer != null) {
      saveConsumer.accept(flowDTO);
    }
  }


}
