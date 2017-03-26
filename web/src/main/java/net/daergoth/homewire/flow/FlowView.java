package net.daergoth.homewire.flow;

import com.vaadin.annotations.Title;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringView(name = FlowView.VIEW_NAME)
@Title("Flows - HomeWire")
public class FlowView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "flows";

  @Autowired
  private FlowService flowService;

  @PostConstruct
  void init() {
    Label header = new Label("Flows");
    header.setStyleName(ValoTheme.LABEL_H1);

    addComponent(header);

    Button testButton = new Button("Save test");
    testButton.addClickListener(event -> {
      FlowDTO flowDTO = new FlowDTO();
      flowDTO.setId(1);
      flowDTO.setName("Test1");
      flowDTO.setOrderNum(2);
      flowDTO.addCondition(new ConditionDTO((short) 1, "temperature", "greaterthan", "23"));
      flowDTO.addAction(new ActionDTO((short) 2, "relay", "set", "0"));
      flowDTO.addAction(new ActionDTO((short) 3, "relay", "set", "0"));

      flowService.saveFlowDto(flowDTO);
    });
    addComponent(testButton);

    List<FlowDTO> flows = flowService.getAllFlowDtos();

    Grid grid = new Grid();
    grid.setContainerDataSource(new BeanItemContainer<>(FlowDTO.class, flows));
    grid.setWidth(90, Unit.PERCENTAGE);

    addComponent(grid);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    // This view is constructed in the init() method()
  }
}
