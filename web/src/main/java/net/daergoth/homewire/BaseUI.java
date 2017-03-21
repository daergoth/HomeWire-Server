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
import net.daergoth.homewire.controlpanel.ControlPanelView;
import net.daergoth.homewire.flow.FlowView;
import net.daergoth.homewire.home.HomeView;
import net.daergoth.homewire.setup.SetupView;
import net.daergoth.homewire.statistic.StatisticView;
import org.vaadin.teemusa.sidemenu.SideMenu;
import org.vaadin.teemusa.sidemenu.SideMenuUI;

@Theme("homewire")
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
    menu.addMenuItem("Control Panel", () -> {
      getUI().getNavigator().navigateTo(ControlPanelView.VIEW_NAME);
    });
    menu.addMenuItem("Flows", () -> {
      getUI().getNavigator().navigateTo(FlowView.VIEW_NAME);
    });
    menu.addMenuItem("Statistics", () -> {
      getUI().getNavigator().navigateTo(StatisticView.VIEW_NAME);
    });
    menu.addMenuItem("Setup", () -> {
      getUI().getNavigator().navigateTo(SetupView.VIEW_NAME);
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