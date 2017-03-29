package net.daergoth.homewire.flow.action.widget;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import net.daergoth.homewire.flow.ActionDTO;
import net.daergoth.homewire.flow.DeviceViewDTO;
import net.daergoth.homewire.setup.DeviceDTO;
import net.daergoth.homewire.setup.DeviceSetupService;
import org.modelmapper.ModelMapper;

import java.util.stream.Collectors;

public class SetActionWidget extends ActionWidget {

  private final DeviceSetupService deviceSetupService;

  private final ModelMapper modelMapper;

  public SetActionWidget(ActionDTO actionDTO, DeviceSetupService deviceSetupService,
                         ModelMapper modelMapper) {
    super(actionDTO);
    this.deviceSetupService = deviceSetupService;
    this.modelMapper = modelMapper;

    if (actionDTO.getActionType() != ActionDTO.ActionTypes.SET) {
      actionDTO.setType("set");
    }

    setup();
  }

  private void setup() {
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

    DeviceDTO selectedDeviceDTO = deviceSetupService
        .getDeviceDtoByIdAndType(actionDTO.getDevId(), actionDTO.getDevType());
    if (selectedDeviceDTO != null) {
      devicesComboBox.select(modelMapper.map(selectedDeviceDTO, DeviceViewDTO.class));
    }

    devicesComboBox.addValueChangeListener(event -> {
      DeviceViewDTO selected = (DeviceViewDTO) event.getProperty().getValue();

      actionDTO.setDevId(selected.getDevId());
      actionDTO.setDevType(selected.getType());

      changeListener.accept(actionDTO);
    });
    mainLayout.addComponent(devicesComboBox);

    // Target
    TextField targetTextField = new TextField("Target", actionDTO.getParameter());
    targetTextField.addValueChangeListener(event -> {
      actionDTO.setParameter((String) event.getProperty().getValue());

      changeListener.accept(actionDTO);
    });
    mainLayout.addComponent(targetTextField);

  }

}
