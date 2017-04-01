package net.daergoth.homewire.controlpanel.component.relay;

import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;
import net.daergoth.homewire.DeviceCommand;
import net.daergoth.homewire.NetworkServer;
import net.daergoth.homewire.controlpanel.component.CustomWidgetFactory;
import net.daergoth.homewire.controlpanel.component.RefreshableWidget;
import net.daergoth.homewire.setup.DeviceDTO;

public class RelayWidgetFactory implements CustomWidgetFactory {

  public static final String CHART_TYPE = "relay";

  private final NetworkServer networkServer;

  public RelayWidgetFactory(NetworkServer networkServer) {
    this.networkServer = networkServer;
  }

  @Override
  public RefreshableWidget createWidget(DeviceDTO device) {
    return new RelayWidget(device.getName(), device.getDevId(), networkServer);
  }

  public static class RelayWidget extends RefreshableWidget<Float> {
    private Label titleLabel;
    private Label typeLabel;
    private NativeButton statusButton;

    public RelayWidget(String title, Short devId, NetworkServer netServer) {
      titleLabel = new Label(title);
      titleLabel.setPrimaryStyleName("live-widget-title");

      statusButton = new NativeButton("Off", event -> {
        netServer.sendDeviceCommand(
            new DeviceCommand(
                devId,
                (Boolean) this.statusButton.getData() ? 0f : 1f
            )
        );
      });
      statusButton.setData(false);
      statusButton.setPrimaryStyleName("relay-widget-button");

      typeLabel = new Label("Relay");
      typeLabel.setPrimaryStyleName("live-widget-type");

      VerticalLayout rootLayout =
          new VerticalLayout(titleLabel, statusButton, typeLabel);
      rootLayout.setPrimaryStyleName("live-widget-root");
      rootLayout.setSizeUndefined();

      setCompositionRoot(rootLayout);
    }

    @Override
    public void refresh(Float value) {
      statusButton.setCaption(value > 1e-5 ? "On" : "Off");
      statusButton.setData(value > 1e-5);
    }

    @Override
    public Class<Float> getRefreshType() {
      return Float.class;
    }
  }

}
