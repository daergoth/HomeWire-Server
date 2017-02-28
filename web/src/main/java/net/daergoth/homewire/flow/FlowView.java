package net.daergoth.homewire.flow;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.PostConstruct;

@SpringView(name = FlowView.VIEW_NAME)
@Title("Flows - HomeWire")
public class FlowView extends VerticalLayout implements View {
  public static final String VIEW_NAME = "flows";

  @PostConstruct
  void init() {
    Label header = new Label("Flows");
    header.setStyleName(ValoTheme.LABEL_H1);

    addComponent(header);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    // This view is constructed in the init() method()
  }
}
