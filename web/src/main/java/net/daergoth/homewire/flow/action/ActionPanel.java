package net.daergoth.homewire.flow.action;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import net.daergoth.homewire.flow.ActionDTO;
import net.daergoth.homewire.flow.TypeViewDTO;
import net.daergoth.homewire.flow.action.widget.ActionWidget;
import net.daergoth.homewire.flow.action.widget.ActionWidgetRepository;

import java.util.Arrays;
import java.util.function.Consumer;

public class ActionPanel extends CustomComponent {

  private final ActionWidgetRepository actionWidgetRepository;

  private final Panel mainPanel;

  private final ComboBox mainTypeComboBox;

  private final ActionDTO actionDTO;

  private ActionWidget currentActionWidget;

  private Consumer<ActionDTO> changeListener;


  public ActionPanel(ActionDTO actionDTO,
                     ActionWidgetRepository actionWidgetRepository,
                     Consumer<ActionDTO> changeListener) {
    this.actionWidgetRepository = actionWidgetRepository;
    this.actionDTO = actionDTO;

    this.mainPanel = new Panel("Action");
    this.mainTypeComboBox = new ComboBox("Type");

    this.currentActionWidget = actionWidgetRepository
        .getFactory(TypeViewDTO.ActionTypes.ofActionDto(actionDTO))
        .createWidget(actionDTO);
    currentActionWidget.setChangeListener(changeListener);

    this.changeListener = changeListener;

    setupPanel();

    setCompositionRoot(mainPanel);
    setSizeUndefined();
  }

  private void setupPanel() {
    mainPanel.setSizeUndefined();

    GridLayout mainGridLayout = new GridLayout(2, 1);

    mainTypeComboBox.setContainerDataSource(new BeanItemContainer<>(
        TypeViewDTO.ActionTypes.class,
        Arrays.asList(TypeViewDTO.ActionTypes.values())
    ));
    mainTypeComboBox.setNullSelectionAllowed(false);
    mainTypeComboBox.select(TypeViewDTO.ActionTypes.ofActionDto(actionDTO));
    mainTypeComboBox.addValueChangeListener(event -> {
      TypeViewDTO.ActionTypes selected =
          (TypeViewDTO.ActionTypes) event.getProperty().getValue();

      currentActionWidget = actionWidgetRepository
          .getFactory(selected)
          .createWidget(actionDTO);
      currentActionWidget.setChangeListener(changeListener);

      mainGridLayout.removeComponent(1, 0);
      mainGridLayout.addComponent(currentActionWidget, 1, 0);
    });
    mainGridLayout.addComponent(mainTypeComboBox, 0, 0);

    mainGridLayout.addComponent(currentActionWidget, 1, 0);

    mainPanel.setContent(mainGridLayout);
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
