package net.daergoth.homewire.flow.action.widget;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import net.daergoth.homewire.flow.persistence.ActionDTO;

import java.util.function.Consumer;

public class ActionWidget extends CustomComponent {

  protected final HorizontalLayout mainLayout;

  protected final ActionDTO actionDTO;

  protected Consumer<ActionDTO> changeListener;

  public ActionWidget(ActionDTO actionDTO) {
    this.actionDTO = actionDTO;
    this.mainLayout = new HorizontalLayout();
    this.changeListener = dto -> {
    };

    setCompositionRoot(mainLayout);
  }

  public HorizontalLayout getMainLayout() {
    return mainLayout;
  }

  public ActionDTO getActionDTO() {
    return actionDTO;
  }

  public Consumer<ActionDTO> getChangeListener() {
    return changeListener;
  }

  public void setChangeListener(
      Consumer<ActionDTO> changeListener) {
    this.changeListener = changeListener;
  }

}
