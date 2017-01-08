package net.daergoth.homewire.live.component;

import net.daergoth.homewire.live.component.justgage.PatchedJustGage;
import org.vaadin.justgage.JustGageConfiguration;

public class TemperatureChartFactory implements CustomChartFactory {

  public static final String CHART_TYPE = "temperature";

  @Override
  public RefreshableChart createChart(String label) {
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

  public static class TemperatureGauge extends RefreshableChart<Float> {
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
