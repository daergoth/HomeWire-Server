package net.daergoth.homewire.configuration;

import net.daergoth.homewire.flow.TypeViewDTO;
import net.daergoth.homewire.flow.action.widget.ActionWidgetRepository;
import net.daergoth.homewire.flow.action.widget.DelayActionWidget;
import net.daergoth.homewire.flow.action.widget.HttpRequestActionWidget;
import net.daergoth.homewire.flow.action.widget.SetActionWidget;
import net.daergoth.homewire.flow.condition.widget.ComparisionConditionWidget;
import net.daergoth.homewire.flow.condition.widget.ConditionWidgetRepository;
import net.daergoth.homewire.flow.condition.widget.HttpRequestConditionWidget;
import net.daergoth.homewire.flow.condition.widget.InterfaceConditionWidget;
import net.daergoth.homewire.flow.condition.widget.TimeConditionWidget;
import net.daergoth.homewire.setup.DeviceSetupService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "net.daergoth.homewire")
@Import(ServiceConfiguration.class)
public class WebConfiguration {

  @Bean
  public ConditionWidgetRepository conditionWidgetRepository(DeviceSetupService deviceSetupService,
                                                             ModelMapper modelMapper) {
    return new ConditionWidgetRepository()
        .addFactory(TypeViewDTO.ConditionTypes.COMPARISION,
            (conditionDTO) -> new ComparisionConditionWidget(conditionDTO, deviceSetupService,
                modelMapper))
        .addFactory(TypeViewDTO.ConditionTypes.REQUEST, HttpRequestConditionWidget::new)
        .addFactory(TypeViewDTO.ConditionTypes.INTERFACE, InterfaceConditionWidget::new)
        .addFactory(TypeViewDTO.ConditionTypes.TIME, TimeConditionWidget::new);
  }

  @Bean
  public ActionWidgetRepository actionWidgetRepository(DeviceSetupService deviceSetupService,
                                                       ModelMapper modelMapper) {
    return new ActionWidgetRepository()
        .addFactory(TypeViewDTO.ActionTypes.SET,
            (actionDTO) -> new SetActionWidget(actionDTO, deviceSetupService, modelMapper))
        .addFactory(TypeViewDTO.ActionTypes.REQUEST, HttpRequestActionWidget::new)
        .addFactory(TypeViewDTO.ActionTypes.DELAY, DelayActionWidget::new);
  }

}
