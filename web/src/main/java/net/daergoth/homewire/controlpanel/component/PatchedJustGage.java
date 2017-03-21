package net.daergoth.homewire.controlpanel.component;

import org.vaadin.justgage.JustGage;
import org.vaadin.justgage.JustGageConfiguration;


public class PatchedJustGage extends JustGage {
  public PatchedJustGage(JustGageConfiguration conf) {
    super(conf);
  }

  public void refresh(float value) {
    this.callFunction("refresh", new Object[]{Float.valueOf(value)});
  }

}
