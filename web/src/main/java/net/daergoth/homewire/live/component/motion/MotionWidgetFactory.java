package net.daergoth.homewire.live.component.motion;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import net.daergoth.homewire.live.component.CustomWidgetFactory;
import net.daergoth.homewire.live.component.RefreshableWidget;

public class MotionWidgetFactory implements CustomWidgetFactory {

  public static final String CHART_TYPE = "motion";

  @Override
  public RefreshableWidget createWidget(String label) {
    return new MotionWidget(label);
  }

  public static class MotionWidget extends RefreshableWidget<Float> {
    private Label titleLabel;
    private Label statusLabel;
    private Label typeLabel;

    public MotionWidget(String title) {
      titleLabel = new Label(title);
      titleLabel.setPrimaryStyleName("live-widget-title");

      statusLabel = new Label("No");
      statusLabel.setPrimaryStyleName("live-widget-data");

      typeLabel = new Label("Motion");
      typeLabel.setPrimaryStyleName("live-widget-type");

      VerticalLayout rootLayout = new VerticalLayout(titleLabel, statusLabel, typeLabel);
      rootLayout.setPrimaryStyleName("live-widget-root");
      rootLayout.setSizeUndefined();

      setCompositionRoot(rootLayout);
    }

    @Override
    public void refresh(Float value) {
      statusLabel.setValue(value != 0 ? "Yes" : "No");
    }

    @Override
    public Class<Float> getRefreshType() {
      return Float.class;
    }
  }

}
