package net.daergoth.homewire.flow.execution.execution;

import net.daergoth.homewire.flow.persistence.ActionDTO;

import java.util.HashMap;
import java.util.Map;

public class ActionExecutingService {

  private Map<ActionDTO.ActionTypes, ActionExecutor> executorMap;

  public ActionExecutingService() {
    this.executorMap = new HashMap<>();
  }

  public void executeAction(ActionDTO actionDTO) {
    if (executorMap.containsKey(actionDTO.getActionType())) {
      executorMap.get(actionDTO.getActionType()).executeAction(actionDTO);
    }
  }

  public ActionExecutingService registerActionExecutor(ActionDTO.ActionTypes actionType,
                                                       ActionExecutor executor) {
    executorMap.put(actionType, executor);

    return this;
  }

}
