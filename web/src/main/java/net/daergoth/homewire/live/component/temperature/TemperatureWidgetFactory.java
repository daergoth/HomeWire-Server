package net.daergoth.homewire.live.component.temperature;

import net.daergoth.homewire.live.component.CustomWidgetFactory;
import net.daergoth.homewire.live.component.RefreshableWidget;
import net.daergoth.homewire.live.component.PatchedJustGage;
import org.vaadin.justgage.JustGageConfiguration;

public class TemperatureWidgetFactory implements CustomWidgetFactory {

  public static final String CHART_TYPE = "temperature";

  @Override
  public RefreshableWidget createWidget(String label) {
    JustGageConfiguration gageConfiguration = getGageConfiguration();
    gageConfiguration.title = label;
    return new TemperatureGauge(new PatchedJustGage(gageConfiguration));
  }

  private JustGageConfiguration getGageConfiguration() {
    JustGageConfiguration gageConfiguration = new JustGageConfiguration();
    gageConfiguration.min = -10f;
    gageConfiguration.max = 45f;
    gageConfiguration.label = "Temperature";
    gageConfiguration.symbol = "°C";
    gageConfiguration.decimals = 1;
    gageConfiguration.relativeGaugeSize = true;

    return gageConfiguration;
  }

  public static class TemperatureGauge extends RefreshableWidget<Float> {
    private PatchedJustGage patchedJustGage;

    public TemperatureGauge(PatchedJustGage patchedJustGage) {
      this.patchedJustGage = patchedJustGage;
      patchedJustGage.setWidth(20, Unit.PERCENTAGE);
      patchedJustGage.setHeight(10, Unit.PERCENTAGE);
      setSizeUndefined();
      setCompositionRoot(patchedJustGage);
    }

    @Override
    public void refresh(Float value) {
      patchedJustGage.refresh(value);
    }

    @Override
    public Class<Float> getRefreshType() {
      return Float.class;
    }
  }

}
