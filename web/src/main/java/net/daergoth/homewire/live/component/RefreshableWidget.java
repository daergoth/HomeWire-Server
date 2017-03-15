package net.daergoth.homewire.live.component;

import com.vaadin.ui.CustomComponent;

public abstract class RefreshableWidget<T> extends CustomComponent {

  public RefreshableWidget() {
    setPrimaryStyleName("live-widget");
  }

  public abstract void refresh(T value);

  public abstract Class<T> getRefreshType();

}
