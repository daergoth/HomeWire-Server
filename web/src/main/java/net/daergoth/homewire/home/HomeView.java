package net.daergoth.homewire.home;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import net.daergoth.homewire.DeviceCommand;
import net.daergoth.homewire.NetworkServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = HomeView.VIEW_NAME)
@Title("Home - HomeWire")
public class HomeView extends VerticalLayout implements View {

  private static final Logger logger = LoggerFactory.getLogger(HomeView.class);

  public static final String VIEW_NAME = "";

  @Autowired
  private NetworkServer networkServer;

  private boolean state = false;

  @PostConstruct
  void init() {
    Label header = new Label("Home");
    header.setStyleName(ValoTheme.LABEL_H1);

    addComponent(header);

    Button testButton = new Button("Toggle relay");
    testButton.addClickListener(event -> {
      logger.warn("Toggle relay!! state: {}", state);
      networkServer.sendDeviceCommand(new DeviceCommand((short) 5, state));
      state = !state;
    });

    addComponent(testButton);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {
    // This view is constructed in the init() method()
  }

}