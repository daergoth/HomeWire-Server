package net.daergoth.homewire.flow.execution.execution;

import net.daergoth.homewire.flow.persistence.ActionDTO;

public interface ActionExecutor {

  void executeAction(ActionDTO actionDTO);

}
