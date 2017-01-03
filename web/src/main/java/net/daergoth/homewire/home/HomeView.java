package net.daergoth.homewire.home;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.PostConstruct;

@SpringView(name = HomeView.VIEW_NAME)
@Title("Home - HomeWire")
public class HomeView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "";

  @PostConstruct
  void init() {
    Label header = new Label("Home");
    header.setStyleName(ValoTheme.LABEL_H1);

    addComponent(header);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {
    // This view is constructed in the init() method()
  }

}