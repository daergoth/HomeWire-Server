package net.daergoth.homewire.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.daergoth.homewire.NetworkServer;
import net.daergoth.homewire.controlpanel.LiveDataRepository;
import net.daergoth.homewire.controlpanel.LiveDataService;
import net.daergoth.homewire.flow.FlowRepository;
import net.daergoth.homewire.flow.execution.FlowExecutorService;
import net.daergoth.homewire.flow.execution.evaluation.ComparisionConditionEvaluator;
import net.daergoth.homewire.flow.execution.evaluation.ConditionEvaluatingService;
import net.daergoth.homewire.flow.execution.execution.ActionExecutingService;
import net.daergoth.homewire.flow.execution.execution.SetActionExecutor;
import net.daergoth.homewire.flow.persistence.ActionDTO;
import net.daergoth.homewire.flow.persistence.ConditionDTO;
import net.daergoth.homewire.flow.persistence.FlowService;
import net.daergoth.homewire.processing.DeviceProcessingService;
import net.daergoth.homewire.processing.ProcessableDeviceDataDTO;
import net.daergoth.homewire.setup.DeviceSetupRepository;
import net.daergoth.homewire.statistic.StatisticDataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.ZonedDateTime;

@Configuration
@ComponentScan(basePackages = "net.daergoth.homewire")
public class ServiceConfiguration {

  @Bean
  public NetworkServer networkServer(ObjectMapper objectMapper) throws IOException {
    return new NetworkServer(objectMapper);
  }

  @Bean
  public DeviceProcessingService deviceProcessingService(
      StatisticDataRepository statisticDataRepository,
      ModelMapper modelMapper,
      LiveDataRepository liveDataRepository,
      DeviceSetupRepository deviceSetupRepository,
      FlowExecutorService flowExecutorService,
      NetworkServer networkServer) {

    DeviceProcessingService deviceProcessingService =
        new DeviceProcessingService(statisticDataRepository, modelMapper, liveDataRepository,
            deviceSetupRepository, flowExecutorService);

    networkServer.setDataProcessor(data -> {
      deviceProcessingService.processDeviceData(
          new ProcessableDeviceDataDTO(data.getId(),
              data.getCategory(),
              data.getType(),
              data.getValue(),
              ZonedDateTime.now()));
    });

    return deviceProcessingService;
  }

  @Bean
  public ConditionEvaluatingService conditionEvaluatingService(LiveDataService liveDataService) {
    return new ConditionEvaluatingService()
        .registerConditionEvaluator(ConditionDTO.ConditionTypes.COMPARISION,
            new ComparisionConditionEvaluator(liveDataService));
  }

  @Bean
  public ActionExecutingService actionExecutingService(LiveDataService liveDataService,
                                                       NetworkServer networkServer) {
    return new ActionExecutingService()
        .registerActionExecutor(ActionDTO.ActionTypes.SET,
            new SetActionExecutor(liveDataService, networkServer));
  }

  @Bean
  public FlowService flowService(FlowRepository flowRepository,
                                 FlowExecutorService flowExecutorService, ModelMapper modelMapper) {
    FlowService flowService = new FlowService(flowRepository, flowExecutorService, modelMapper);

    flowService.getAllFlowDtos().forEach(flowExecutorService::processFlowChange);

    return flowService;
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
