package net.daergoth.homewire.flow;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.*;
import net.daergoth.homewire.setup.DeviceDTO;
import net.daergoth.homewire.setup.DeviceSetupService;
import org.vaadin.addons.stackpanel.StackPanel;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class FlowViewPanel extends CustomComponent {

  private Panel innerPanel;

  private final FlowDTO flowDTO;

  private Consumer<FlowDTO> saveConsumer;

  private final DeviceSetupService deviceSetupService;

  public FlowViewPanel(FlowDTO flowDTO, DeviceSetupService deviceSetupService) {
    this.flowDTO = flowDTO;
    this.innerPanel = new Panel(flowDTO.getName());
    this.deviceSetupService = deviceSetupService;

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
    panelGrid.addComponent(new Label("Conditions"), 0, 2);

    // Actions label
    panelGrid.addComponent(new Label("Actions"), 1, 2);

    // ROW 4
    // Condition list
    VerticalLayout conditionsVerticalLayout = new VerticalLayout();
    flowDTO.getConditionList().forEach(conditionDTO -> {
      // Devices
      DeviceDtoConverter converter = new DeviceDtoConverter();

      ComboBox devicesComboBox = new ComboBox("Devices");
      devicesComboBox.setNullSelectionItemId(false);
      devicesComboBox.setContainerDataSource(
          new BeanItemContainer<DeviceView>(
              DeviceView.class,
              deviceSetupService.getAllDeviceDtos().stream()
                  .map(deviceDTO -> converter.convertToPresentation(deviceDTO, DeviceView.class, Locale.getDefault()))
                  .collect(Collectors.toList())
          )
      );
      devicesComboBox.select(
          converter.convertToPresentation(
              deviceSetupService.getDeviceDtoByIdAndType(conditionDTO.getDevId(), conditionDTO.getDevType()),
              DeviceView.class,
              Locale.getDefault()
          )
      );
      // Device listing only needed if the condition is comparision type
      devicesComboBox.setVisible(false);
      devicesComboBox.addValueChangeListener(event -> {
        DeviceView selected = (DeviceView) event.getProperty().getValue();

        int i = flowDTO.getConditionList().indexOf(conditionDTO);
        flowDTO.removeCondition(conditionDTO);

        conditionDTO.setDevId(selected.getDevId());
        conditionDTO.setDevType(selected.getDevType());

        flowDTO.addCondition(i, conditionDTO);

        saveIfPossible();
      });
      devicesComboBox.setVisible(conditionDTO.getConditionType() == ConditionDTO.ConditionTypes.Comparision);

      // Types
      List<ConditionOrActionTypeView> conditionTypeViews = Arrays.asList(
          new ConditionOrActionTypeView("greaterthan", ">"),
          new ConditionOrActionTypeView("greaterthanequal", ">="),
          new ConditionOrActionTypeView("lessthan", "<"),
          new ConditionOrActionTypeView("lessthanequal", "<="),
          new ConditionOrActionTypeView("equal", "="),
          new ConditionOrActionTypeView("notequal", "!="),
          new ConditionOrActionTypeView("request", "HTTP request"),
          new ConditionOrActionTypeView("interface", "Button"),
          new ConditionOrActionTypeView("time", "Time")
      );

      ComboBox typeComboBox = new ComboBox("Type");
      typeComboBox.setNullSelectionAllowed(false);
      typeComboBox.addItems(conditionTypeViews);
      typeComboBox.setValue(conditionTypeViews
          .stream()
          .filter(conditionTypeView -> conditionTypeView.getRawType().equals(conditionDTO.getType()))
          .findFirst()
          .get()
      );
      typeComboBox.addValueChangeListener(event -> {
        ConditionOrActionTypeView selected = (ConditionOrActionTypeView) event.getProperty().getValue();

        int i = flowDTO.getConditionList().indexOf(conditionDTO);
        flowDTO.removeCondition(conditionDTO);

        conditionDTO.setType(selected.getRawType());

        if (conditionDTO.getConditionType() == ConditionDTO.ConditionTypes.Comparision) {
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

      HorizontalLayout conditionRow = new HorizontalLayout(devicesComboBox, typeComboBox, parameterTextField);

      conditionsVerticalLayout.addComponent(conditionRow);
    });
    panelGrid.addComponent(conditionsVerticalLayout, 0, 3);

    // Action list
    VerticalLayout actionsVerticalLayout = new VerticalLayout();
    flowDTO.getActionList().forEach(actionDTO -> {
      // Devices
      DeviceDtoConverter converter = new DeviceDtoConverter();

      ComboBox devicesComboBox = new ComboBox("Actors");
      devicesComboBox.setNullSelectionItemId(false);
      devicesComboBox.setContainerDataSource(
          new BeanItemContainer<DeviceView>(
              DeviceView.class,
              deviceSetupService.getAllActors().stream()
                  .map(deviceDTO -> converter.convertToPresentation(deviceDTO, DeviceView.class, Locale.getDefault()))
                  .collect(Collectors.toList())
          )
      );
      devicesComboBox.select(
          converter.convertToPresentation(
              deviceSetupService.getDeviceDtoByIdAndType(actionDTO.getDevId(), actionDTO.getDevType()),
              DeviceView.class,
              Locale.getDefault()
          )
      );
      // Device listing only needed if the condition is comparision type
      devicesComboBox.setVisible(false);
      devicesComboBox.addValueChangeListener(event -> {
        DeviceView selected = (DeviceView) event.getProperty().getValue();

        int i = flowDTO.getActionList().indexOf(actionDTO);
        flowDTO.removeAction(actionDTO);

        actionDTO.setDevId(selected.getDevId());
        actionDTO.setDevType(selected.getDevType());

        flowDTO.addAction(i, actionDTO);

        saveIfPossible();
      });
      devicesComboBox.setVisible(actionDTO.getActionType() == ActionDTO.ActionTypes.Set);

      // Types
      List<ConditionOrActionTypeView> actionTypeViews = Arrays.asList(
          new ConditionOrActionTypeView("set", "Set"),
          new ConditionOrActionTypeView("request", "HTTP request"),
          new ConditionOrActionTypeView("delay", "Delay")
      );

      ComboBox typeComboBox = new ComboBox("Type");
      typeComboBox.setNullSelectionAllowed(false);
      typeComboBox.addItems(actionTypeViews);
      typeComboBox.setValue(actionTypeViews
          .stream()
          .filter(actionTypeView -> actionTypeView.getRawType().equals(actionDTO.getType()))
          .findFirst()
          .get()
      );
      typeComboBox.addValueChangeListener(event -> {
        ConditionOrActionTypeView selected = (ConditionOrActionTypeView) event.getProperty().getValue();

        int i = flowDTO.getActionList().indexOf(actionDTO);
        flowDTO.removeAction(actionDTO);

        actionDTO.setType(selected.getRawType());

        if (actionDTO.getActionType() == ActionDTO.ActionTypes.Set) {
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

      HorizontalLayout actionRow = new HorizontalLayout(devicesComboBox, typeComboBox, parameterTextField);

      actionsVerticalLayout.addComponent(actionRow);
    });
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

  private static class DeviceView {
    private final Short devId;

    private final String devType;

    private final String name;

    public DeviceView(Short devId, String devType, String name) {
      this.devId = devId;
      this.devType = devType;
      this.name = name;
    }

    public Short getDevId() {
      return devId;
    }

    public String getDevType() {
      return devType;
    }

    public String getName() {
      return name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      DeviceView that = (DeviceView) o;

      if (!devId.equals(that.devId)) return false;
      if (!devType.equals(that.devType)) return false;
      return name.equals(that.name);
    }

    @Override
    public int hashCode() {
      int result = devId.hashCode();
      result = 31 * result + devType.hashCode();
      result = 31 * result + name.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return name +
          " - " +
          devType.substring(0, 1).toUpperCase() + devType.substring(1);
    }
  }

  private static class ConditionOrActionTypeView {
    private final String rawType;

    private final String name;

    public ConditionOrActionTypeView(String rawType, String name) {
      this.rawType = rawType;
      this.name = name;
    }

    public String getRawType() {
      return rawType;
    }

    public String getName() {
      return name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      ConditionOrActionTypeView that = (ConditionOrActionTypeView) o;

      if (!rawType.equals(that.rawType)) return false;
      return name.equals(that.name);
    }

    @Override
    public int hashCode() {
      int result = rawType.hashCode();
      result = 31 * result + name.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  private class DeviceDtoConverter implements Converter<DeviceView, DeviceDTO> {

    @Override
    public DeviceDTO convertToModel(DeviceView value, Class<? extends DeviceDTO> targetType, Locale locale) throws
        Converter.ConversionException {
      if (value == null) {
        return null;
      }

      DeviceView v = (DeviceView) value;
      return deviceSetupService.getDeviceDtoByIdAndType(v.getDevId(), v.getDevType());
    }

    @Override
    public DeviceView convertToPresentation(DeviceDTO value, Class<? extends DeviceView> targetType, Locale locale) throws
        Converter.ConversionException {
      if (value == null) {
        return null;
      }
      return new DeviceView(value.getDevId(), value.getType(), value.getName());
    }

    @Override
    public Class<DeviceDTO> getModelType() {
      return DeviceDTO.class;
    }

    @Override
    public Class<DeviceView> getPresentationType() {
      return DeviceView.class;
    }
  }

}
