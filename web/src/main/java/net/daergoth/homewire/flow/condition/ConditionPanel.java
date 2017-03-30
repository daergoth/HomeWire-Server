package net.daergoth.homewire.flow.condition;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import net.daergoth.homewire.flow.persistence.ConditionDTO;
import net.daergoth.homewire.flow.TypeViewDTO;
import net.daergoth.homewire.flow.condition.widget.ConditionWidget;
import net.daergoth.homewire.flow.condition.widget.ConditionWidgetRepository;

import java.util.Arrays;
import java.util.function.Consumer;

public class ConditionPanel extends CustomComponent {

  private final ConditionWidgetRepository conditionWidgetRepository;

  private final Panel mainPanel;

  private final ComboBox mainTypeComboBox;

  private final ConditionDTO conditionDTO;

  private ConditionWidget currentConditionWidget;

  private Consumer<ConditionDTO> changeListener;


  public ConditionPanel(ConditionDTO conditionDTO,
                        ConditionWidgetRepository conditionWidgetRepository,
                        Consumer<ConditionDTO> changeListener) {
    this.conditionWidgetRepository = conditionWidgetRepository;
    this.conditionDTO = conditionDTO;

    this.mainPanel = new Panel("Condition");
    this.mainTypeComboBox = new ComboBox("Type");

    this.currentConditionWidget = conditionWidgetRepository
        .getFactory(TypeViewDTO.ConditionTypes.ofConditionDto(conditionDTO))
        .createWidget(conditionDTO);
    currentConditionWidget.setChangeListener(changeListener);

    this.changeListener = changeListener;

    setupPanel();

    setCompositionRoot(mainPanel);
    setSizeUndefined();
  }

  private void setupPanel() {
    mainPanel.setSizeUndefined();

    GridLayout mainGridLayout = new GridLayout(2, 1);

    mainTypeComboBox.setContainerDataSource(new BeanItemContainer<>(
        TypeViewDTO.ConditionTypes.class,
        Arrays.asList(TypeViewDTO.ConditionTypes.values())
    ));
    mainTypeComboBox.setNullSelectionAllowed(false);
    mainTypeComboBox.select(TypeViewDTO.ConditionTypes.ofConditionDto(conditionDTO));
    mainTypeComboBox.addValueChangeListener(event -> {
      TypeViewDTO.ConditionTypes selected =
          (TypeViewDTO.ConditionTypes) event.getProperty().getValue();

      currentConditionWidget = conditionWidgetRepository
          .getFactory(selected)
          .createWidget(conditionDTO);
      currentConditionWidget.setChangeListener(changeListener);

      mainGridLayout.removeComponent(1, 0);
      mainGridLayout.addComponent(currentConditionWidget, 1, 0);
    });

    Button deleteButton = new Button("Delete", event -> {
      changeListener.accept(null);
    });

    mainGridLayout.addComponent(new VerticalLayout(mainTypeComboBox, deleteButton), 0, 0);

    mainGridLayout.addComponent(currentConditionWidget, 1, 0);

    mainPanel.setContent(mainGridLayout);
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
