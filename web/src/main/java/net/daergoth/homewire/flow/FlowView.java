package net.daergoth.homewire.flow;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import net.daergoth.homewire.flow.action.widget.ActionWidgetRepository;
import net.daergoth.homewire.flow.condition.widget.ConditionWidgetRepository;
import net.daergoth.homewire.setup.DeviceSetupService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import javax.annotation.PostConstruct;

@SpringView(name = FlowView.VIEW_NAME)
@Title("Flows - HomeWire")
public class FlowView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "flows";

  @Autowired
  private FlowService flowService;

  @Autowired
  private DeviceSetupService deviceSetupService;

  @Autowired
  private ConditionWidgetRepository conditionWidgetRepository;

  @Autowired
  private ActionWidgetRepository actionWidgetRepository;

  @Autowired
  private ModelMapper modelMapper;

  @PostConstruct
  void init() {
    Label header = new Label("Flows");
    header.setStyleName(ValoTheme.LABEL_H1);

    addComponent(header);

    List<FlowDTO> flows = flowService.getAllFlowDtos();

    flows.forEach(flowDTO -> {
      FlowViewPanel flowViewPanel = new FlowViewPanel(flowDTO, deviceSetupService, modelMapper,
          conditionWidgetRepository, actionWidgetRepository);
      flowViewPanel.setSaveConsumer(dto -> flowService.saveFlowDto(dto));
      addComponent(flowViewPanel);
    });
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    // This view is constructed in the init() method()
  }

}
