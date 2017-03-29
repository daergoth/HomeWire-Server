package net.daergoth.homewire.flow.condition.widget;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import net.daergoth.homewire.flow.ConditionDTO;

import java.util.function.Consumer;

public class ConditionWidget extends CustomComponent {

  protected final HorizontalLayout mainLayout;

  protected final ConditionDTO conditionDTO;

  protected Consumer<ConditionDTO> changeListener;

  public ConditionWidget(ConditionDTO conditionDTO) {
    this.conditionDTO = conditionDTO;
    this.mainLayout = new HorizontalLayout();
    this.changeListener = dto -> {};

    setCompositionRoot(mainLayout);
  }

  public HorizontalLayout getMainLayout() {
    return mainLayout;
  }

  public ConditionDTO getConditionDTO() {
    return conditionDTO;
  }

  public Consumer<ConditionDTO> getChangeListener() {
    return changeListener;
  }

  public void setChangeListener(
      Consumer<ConditionDTO> changeListener) {
    this.changeListener = changeListener;
  }
}
