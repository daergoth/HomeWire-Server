package net.daergoth.homewire.controlpanel.component.soilmoisture;

import net.daergoth.homewire.controlpanel.component.CustomWidgetFactory;
import net.daergoth.homewire.controlpanel.component.PatchedJustGage;
import net.daergoth.homewire.controlpanel.component.RefreshableWidget;
import net.daergoth.homewire.setup.DeviceDTO;
import org.vaadin.justgage.JustGageConfiguration;

public class SoilMoistureWidgetFactory implements CustomWidgetFactory {

  public static final String CHART_TYPE = "soilmoisture";

  @Override
  public RefreshableWidget createWidget(DeviceDTO device) {
    JustGageConfiguration gageConfiguration = getGageConfiguration();
    gageConfiguration.title = device.getName();
    return new SoilMoistureGauge(new PatchedJustGage(gageConfiguration));
  }

  private JustGageConfiguration getGageConfiguration() {
    JustGageConfiguration gageConfiguration = new JustGageConfiguration();
    gageConfiguration.min = 0f;
    gageConfiguration.max = 100f;
    gageConfiguration.label = "Soil moisture";
    gageConfiguration.symbol = "%";
    gageConfiguration.decimals = 1;
    gageConfiguration.levelColors = new String[]{"#0b99d7", "#28d30a", "#d23f0a"};
    gageConfiguration.relativeGaugeSize = true;

    return gageConfiguration;
  }

  public static class SoilMoistureGauge extends RefreshableWidget<Float> {

    private PatchedJustGage patchedJustGage;

    public SoilMoistureGauge(PatchedJustGage patchedJustGage) {
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
