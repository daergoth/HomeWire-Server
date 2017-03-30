package net.daergoth.homewire.flow.execution.execution;


import net.daergoth.homewire.DeviceCommand;
import net.daergoth.homewire.NetworkServer;
import net.daergoth.homewire.controlpanel.LiveDataService;
import net.daergoth.homewire.flow.persistence.ActionDTO;
import org.springframework.beans.factory.annotation.Autowired;

public class SetActionExecutor implements ActionExecutor {

  private final LiveDataService liveDataService;

  private final NetworkServer networkServer;

  @Autowired
  public SetActionExecutor(LiveDataService liveDataService, NetworkServer networkServer) {
    this.liveDataService = liveDataService;
    this.networkServer = networkServer;
  }

  @Override
  public void executeAction(ActionDTO actionDTO) {
    Float currentValue =
        liveDataService
            .getCurrentDeviceDataForIdAndType(actionDTO.getDevId(), actionDTO.getDevType())
            .getValue();

    Float target = Float.parseFloat(actionDTO.getParameter());

    if (!currentValue.equals(target)) {
      networkServer.sendDeviceCommand(new DeviceCommand(actionDTO.getDevId(), target));
    }
  }

}
