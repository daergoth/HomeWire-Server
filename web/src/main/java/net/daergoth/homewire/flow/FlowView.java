package net.daergoth.homewire.flow;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import net.daergoth.homewire.flow.action.widget.ActionWidgetRepository;
import net.daergoth.homewire.flow.condition.widget.ConditionWidgetRepository;
import net.daergoth.homewire.flow.persistence.FlowDTO;
import net.daergoth.homewire.flow.persistence.FlowService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = FlowView.VIEW_NAME)
@Title("Flows - HomeWire")
public class FlowView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "flows";

  @Autowired
  private FlowService flowService;

  @Autowired
  private ConditionWidgetRepository conditionWidgetRepository;

  @Autowired
  private ActionWidgetRepository actionWidgetRepository;

  private VerticalLayout flowsRootLayout;

  @PostConstruct
  void init() {
    flowsRootLayout = new VerticalLayout();

    Label header = new Label("Flows");
    header.setStyleName(ValoTheme.LABEL_H1);

    addComponent(header);

    Button newFlowButton = new Button("New Flow", event -> {
      FlowDTO newFlowDto = new FlowDTO();
      newFlowDto.setName("New Flow");
      newFlowDto.setOrderNum(0);

      flowService.saveFlowDto(newFlowDto);

      generateFlows();
    });
    addComponent(newFlowButton);

    generateFlows();

    addComponent(flowsRootLayout);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    // This view is constructed in the init() method()
  }

  private void generateFlows() {
    flowsRootLayout.removeAllComponents();

    flowService.getAllFlowDtos().forEach(flowDTO -> {
      FlowViewPanel flowViewPanel = new FlowViewPanel(flowDTO,
          conditionWidgetRepository, actionWidgetRepository);
      flowViewPanel.setSaveConsumer(dto -> {
        if (dto == null) {
          flowService.removeFlowDto(flowDTO.getId());

          generateFlows();
        } else {
          flowService.saveFlowDto(dto);
        }
      });
      flowsRootLayout.addComponent(flowViewPanel);
    });
  }

}
