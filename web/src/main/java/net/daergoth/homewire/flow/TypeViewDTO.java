package net.daergoth.homewire.flow;

import net.daergoth.homewire.flow.persistence.ActionDTO;
import net.daergoth.homewire.flow.persistence.ConditionDTO;

public class TypeViewDTO {

  public enum ConditionTypes {
    COMPARISION("comparision", "COMPARISION"),
    REQUEST("request", "HTTP request"),
    INTERFACE("interface", "Button"),
    TIME("time", "TIME");

    private final String rawType;

    private final String name;

    ConditionTypes(String rawType, String name) {
      this.rawType = rawType;
      this.name = name;
    }

    public static ConditionTypes ofConditionDto(ConditionDTO conditionDTO) {
      return ConditionTypes.valueOf(conditionDTO.getConditionType().toString().toUpperCase());
    }

    public String getRawType() {
      return rawType;
    }

    public String getName() {
      return name;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  public enum ActionTypes {
    SET("set", "SET"),
    REQUEST("request", "HTTP request"),
    DELAY("delay", "DELAY");

    private final String rawType;

    private final String name;

    ActionTypes(String rawType, String name) {
      this.rawType = rawType;
      this.name = name;
    }

    public static ActionTypes ofActionDto(ActionDTO actionDTO) {
      return ActionTypes.valueOf(actionDTO.getActionType().toString().toUpperCase());
    }

    public String getRawType() {
      return rawType;
    }

    public String getName() {
      return name;
    }

    @Override
    public String toString() {
      return name;
    }
  }

}