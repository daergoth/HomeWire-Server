package net.daergoth.homewire.live.component.relay;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import net.daergoth.homewire.live.component.CustomWidgetFactory;
import net.daergoth.homewire.live.component.RefreshableWidget;

public class RelayWidgetFactory implements CustomWidgetFactory {

  public static final String CHART_TYPE = "relay";

  @Override
  public RefreshableWidget createWidget(String label) {
    return new RelayWidget(label);
  }

  public static class RelayWidget extends RefreshableWidget<Float> {
    private Label titleLabel;
    private Label statusLabel;
    private Label typeLabel;

    public RelayWidget(String title) {
      titleLabel = new Label(title);
      titleLabel.setPrimaryStyleName("live-widget-title");

      statusLabel = new Label("Off");
      statusLabel.setPrimaryStyleName("live-widget-data");

      typeLabel = new Label("Relay");
      typeLabel.setPrimaryStyleName("live-widget-type");

      VerticalLayout rootLayout = new VerticalLayout(titleLabel, statusLabel, typeLabel);
      rootLayout.setPrimaryStyleName("live-widget-root");
      rootLayout.setSizeUndefined();

      setCompositionRoot(rootLayout);
    }

    @Override
    public void refresh(Float value) {
      statusLabel.setValue(value != 0 ? "On" : "Off");
    }

    @Override
    public Class<Float> getRefreshType() {
      return Float.class;
    }
  }

}
