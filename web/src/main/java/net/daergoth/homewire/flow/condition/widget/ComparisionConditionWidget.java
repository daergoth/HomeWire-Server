package net.daergoth.homewire.flow.condition.widget;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import net.daergoth.homewire.flow.ConditionDTO;
import net.daergoth.homewire.flow.DeviceViewDTO;
import net.daergoth.homewire.setup.DeviceDTO;
import net.daergoth.homewire.setup.DeviceSetupService;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ComparisionConditionWidget extends ConditionWidget {

  private final DeviceSetupService deviceSetupService;

  private final ModelMapper modelMapper;

  public ComparisionConditionWidget(ConditionDTO conditionDTO,
                                    DeviceSetupService deviceSetupService,
                                    ModelMapper modelMapper) {
    super(conditionDTO);

    this.deviceSetupService = deviceSetupService;
    this.modelMapper = modelMapper;

    if (conditionDTO.getConditionType() != ConditionDTO.ConditionTypes.COMPARISION) {
      conditionDTO.setType("lessthan");
    }

    setup();
  }

  private void setup() {
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

    DeviceDTO selectedDeviceDTO = deviceSetupService
        .getDeviceDtoByIdAndType(conditionDTO.getDevId(), conditionDTO.getDevType());
    if (selectedDeviceDTO != null) {
      devicesComboBox.select(modelMapper.map(selectedDeviceDTO, DeviceViewDTO.class));
    }

    devicesComboBox.addValueChangeListener(event -> {
      DeviceViewDTO selected = (DeviceViewDTO) event.getProperty().getValue();

      conditionDTO.setDevId(selected.getDevId());
      conditionDTO.setDevType(selected.getType());

      changeListener.accept(conditionDTO);
    });
    mainLayout.addComponent(devicesComboBox);

    // Types
    ComboBox typesComboBox = new ComboBox("Relation");
    typesComboBox.setNullSelectionAllowed(false);
    typesComboBox.setContainerDataSource(
        new BeanItemContainer<>(ComparisionType.class, ComparisionType.ALL));
    typesComboBox.select(
        ComparisionType.ALL.stream()
            .filter(comparisionType -> comparisionType.getType().equals(conditionDTO.getType()))
            .findFirst()
            .get()
    );
    typesComboBox.addValueChangeListener(event -> {
      ComparisionType selected = (ComparisionType) event.getProperty().getValue();

      conditionDTO.setType(selected.getType());

      changeListener.accept(conditionDTO);
    });
    mainLayout.addComponent(typesComboBox);

    // Threshold
    TextField thresholdTextField = new TextField("Threshold", conditionDTO.getParameter());
    thresholdTextField.addValueChangeListener(event -> {
      conditionDTO.setParameter((String) event.getProperty().getValue());

      changeListener.accept(conditionDTO);
    });
    mainLayout.addComponent(thresholdTextField);
  }

  private static class ComparisionType {

    public static final List<ComparisionType> ALL = Arrays.asList(
        new ComparisionType("lessthan", "<"),
        new ComparisionType("lessthanequal", "<="),
        new ComparisionType("greaterthan", ">"),
        new ComparisionType("greaterthanequal", ">="),
        new ComparisionType("equal", "="),
        new ComparisionType("notequal", "!=")
    );

    private final String type;

    private final String name;

    public ComparisionType(String type, String name) {
      this.type = type;
      this.name = name;
    }

    public String getType() {
      return type;
    }

    public String getName() {
      return name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      ComparisionType that = (ComparisionType) o;

      if (!type.equals(that.type)) {
        return false;
      }
      return name.equals(that.name);
    }

    @Override
    public int hashCode() {
      int result = type.hashCode();
      result = 31 * result + name.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return name;
    }
  }

}
