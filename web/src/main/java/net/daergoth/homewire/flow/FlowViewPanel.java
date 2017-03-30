package net.daergoth.homewire.flow;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import net.daergoth.homewire.flow.action.ActionPanel;
import net.daergoth.homewire.flow.action.widget.ActionWidgetRepository;
import net.daergoth.homewire.flow.condition.ConditionPanel;
import net.daergoth.homewire.flow.condition.widget.ConditionWidgetRepository;
import net.daergoth.homewire.flow.persistence.ActionDTO;
import net.daergoth.homewire.flow.persistence.ConditionDTO;
import net.daergoth.homewire.flow.persistence.FlowDTO;
import org.vaadin.addons.stackpanel.StackPanel;

import java.util.function.Consumer;


public class FlowViewPanel extends CustomComponent {

  private Panel innerPanel;

  private final FlowDTO flowDTO;

  private Consumer<FlowDTO> saveConsumer;

  private final ConditionWidgetRepository conditionWidgetRepository;

  private final ActionWidgetRepository actionWidgetRepository;

  public FlowViewPanel(FlowDTO flowDTO,
                       ConditionWidgetRepository conditionWidgetRepository,
                       ActionWidgetRepository actionWidgetRepository) {
    this.flowDTO = flowDTO;
    this.innerPanel = new Panel(flowDTO.getName());
    this.conditionWidgetRepository = conditionWidgetRepository;
    this.actionWidgetRepository = actionWidgetRepository;

    StackPanel stackPanel = StackPanel.extend(innerPanel);
    stackPanel.setToggleIconsEnabled(false);
    stackPanel.close();

    innerPanel.setContent(getPanelGrid());

    setCompositionRoot(innerPanel);
  }

  private GridLayout getPanelGrid() {
    GridLayout panelGrid = new GridLayout(2, 5);
    panelGrid.setWidth(100, Unit.PERCENTAGE);

    // ROW 1
    // Name TextField
    TextField nameTextField = new TextField("Name", flowDTO.getName());
    nameTextField.addValueChangeListener(event -> {
      if (event.getProperty().getType() == String.class) {
        String newName = (String) event.getProperty().getValue();

        flowDTO.setName(newName);
        innerPanel.setCaption(newName);

        saveIfPossible();
      }
    });
    panelGrid.addComponent(nameTextField, 0, 0);

    // Delete flow button
    Button deleteFlow = new Button("Delete Flow", event -> {
      saveConsumer.accept(null);
    });
    panelGrid.addComponent(deleteFlow, 1, 0);

    // ROW 2
    // Order number
    Label orderTitleLabel = new Label("Order number: ");
    Label orderNumLabel = new Label(String.valueOf(flowDTO.getOrderNum()));
    Button orderNumUpButton = new Button("^", event -> {
      int newOrderNum = flowDTO.getOrderNum() + 1;

      orderNumLabel.setValue(String.valueOf(newOrderNum));
      flowDTO.setOrderNum(newOrderNum);

      saveIfPossible();
    });
    Button orderNumDownButton = new Button("˘", event -> {
      int newOrderNum = flowDTO.getOrderNum() - 1;

      if (newOrderNum < 0) {
        newOrderNum = 0;
      }

      orderNumLabel.setValue(String.valueOf(newOrderNum));
      flowDTO.setOrderNum(newOrderNum);

      saveIfPossible();
    });
    panelGrid.addComponent(
        new HorizontalLayout(orderTitleLabel, orderNumLabel, orderNumUpButton, orderNumDownButton),
        0, 1);

    // ROW 3
    // Conditions label
    Label conditionsLabel = new Label("Conditions");
    conditionsLabel.setSizeUndefined();
    panelGrid.addComponent(conditionsLabel, 0, 2);

    // Actions label
    panelGrid.addComponent(new Label("Actions"), 1, 2);

    // ROW 4
    // Condition list
    generateConditionList(panelGrid);

    // Action list
    generateActionList(panelGrid);

    // ROW 5
    // New condition button
    Button newConditionButton = new Button("New Condition", event -> {
      ConditionDTO lastConditionDTO;
      Integer lastConditionIndex = flowDTO.getConditionList().size() - 1;

      if (lastConditionIndex > -1) {
        lastConditionDTO = flowDTO.getConditionList().get(lastConditionIndex);
      } else {
        lastConditionDTO = new ConditionDTO((short) 0, "", "lessthan", "0");
      }

      flowDTO.addCondition(
          new ConditionDTO(lastConditionDTO.getDevId(), lastConditionDTO.getDevType(),
              lastConditionDTO.getType(), lastConditionDTO.getParameter()));

      generateConditionList(panelGrid);
    });
    panelGrid.addComponent(newConditionButton, 0, 4);

    // New action button
    Button newActionButton = new Button("New Action", event -> {
      ActionDTO lastActionDTO;
      Integer lastActionIndex = flowDTO.getActionList().size() - 1;
      if (lastActionIndex > -1) {
        lastActionDTO = flowDTO.getActionList().get(lastActionIndex);
      } else {
        lastActionDTO = new ActionDTO((short) 0, "", "set", "0");
      }

      flowDTO.addAction(new ActionDTO(lastActionDTO.getDevId(), lastActionDTO.getDevType(),
          lastActionDTO.getType(), lastActionDTO.getParameter()));

      generateActionList(panelGrid);
    });
    panelGrid.addComponent(newActionButton, 1, 4);

    return panelGrid;
  }

  private void generateConditionList(GridLayout panelGrid) {
    VerticalLayout conditionsVerticalLayout = new VerticalLayout();
    conditionsVerticalLayout.setSizeUndefined();

    flowDTO.getConditionList().forEach(
        conditionDTO -> conditionsVerticalLayout
            .addComponent(new ConditionPanel(conditionDTO, conditionWidgetRepository,
                condDto -> {
                  if (condDto == null) {
                    flowDTO.removeCondition(conditionDTO);
                    generateConditionList(panelGrid);
                  }

                  saveIfPossible();
                }
            )));

    panelGrid.removeComponent(0, 3);
    panelGrid.addComponent(conditionsVerticalLayout, 0, 3);
  }

  private void generateActionList(GridLayout panelGrid) {
    VerticalLayout actionsVerticalLayout = new VerticalLayout();
    actionsVerticalLayout.setSizeUndefined();

    flowDTO.getActionList().forEach(actionDTO -> actionsVerticalLayout.addComponent(
        new ActionPanel(actionDTO, actionWidgetRepository, actDto -> {
          if (actDto == null) {
            flowDTO.removeAction(actionDTO);
            generateActionList(panelGrid);
          }

          saveIfPossible();
        })));

    panelGrid.removeComponent(1, 3);
    panelGrid.addComponent(actionsVerticalLayout, 1, 3);
  }

  public void setSaveConsumer(Consumer<FlowDTO> saveConsumer) {
    this.saveConsumer = saveConsumer;
  }

  private void saveIfPossible() {
    if (saveConsumer != null) {
      saveConsumer.accept(flowDTO);
    }
  }


}
