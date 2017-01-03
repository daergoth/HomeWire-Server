package net.daergoth.homewire;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import net.daergoth.homewire.home.HomeView;
import net.daergoth.homewire.live.LiveView;
import org.vaadin.teemusa.sidemenu.SideMenu;
import org.vaadin.teemusa.sidemenu.SideMenuUI;

@Theme("valo")
@SpringUI
@SideMenuUI
@SpringViewDisplay
public class BaseUI extends UI implements ViewDisplay {

  private VerticalLayout springViewDisplay;

  @Override
  protected void init(VaadinRequest request) {

    SideMenu menu = new SideMenu();
    menu.setMenuCaption("HomeWire");
    menu.addMenuItem("Home", () -> {
      getUI().getNavigator().navigateTo(HomeView.VIEW_NAME);
    });
    menu.addMenuItem("Live data", () -> {
      getUI().getNavigator().navigateTo(LiveView.VIEW_NAME);
    });
    setContent(menu);

    springViewDisplay = new VerticalLayout();
    springViewDisplay.setSizeFull();
    springViewDisplay.setMargin(true);
    springViewDisplay.setSpacing(true);

    menu.setContent(springViewDisplay);

  }

  @Override
  public void showView(View view) {
    springViewDisplay.removeAllComponents();
    springViewDisplay.addComponent((Component) view);
  }

}