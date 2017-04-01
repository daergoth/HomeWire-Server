package net.daergoth.homewire.controlpanel.component.temperature;

import net.daergoth.homewire.controlpanel.component.CustomWidgetFactory;
import net.daergoth.homewire.controlpanel.component.PatchedJustGage;
import net.daergoth.homewire.controlpanel.component.RefreshableWidget;
import net.daergoth.homewire.setup.DeviceDTO;
import org.vaadin.justgage.JustGageConfiguration;

public class TemperatureWidgetFactory implements CustomWidgetFactory {

  public static final String CHART_TYPE = "temperature";

  @Override
  public RefreshableWidget createWidget(DeviceDTO device) {
    JustGageConfiguration gageConfiguration = getGageConfiguration();

    if (device == null) {
      gageConfiguration.title = "DELETED";
    } else {
      gageConfiguration.title = device.getName();
    }
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
