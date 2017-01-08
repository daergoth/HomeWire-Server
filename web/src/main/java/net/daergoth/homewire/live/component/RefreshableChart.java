package net.daergoth.homewire.live.component;

import com.vaadin.ui.CustomComponent;

public abstract class RefreshableChart<T> extends CustomComponent {

  public abstract void refresh(T value);

  public abstract Class<T> getRefreshType();

}
