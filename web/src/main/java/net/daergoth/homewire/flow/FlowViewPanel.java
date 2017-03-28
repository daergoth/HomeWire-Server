package net.daergoth.homewire.flow;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import org.vaadin.addons.stackpanel.StackPanel;

import java.util.function.Consumer;


public class FlowViewPanel extends CustomComponent {

  private Panel innerPanel;

  private FlowDTO flowDTO;

  private Consumer<FlowDTO> saveConsumer;

  public FlowViewPanel(FlowDTO flowDTO) {
    this.flowDTO = flowDTO;
    this.innerPanel = new Panel(flowDTO.getName());

    StackPanel.extend(innerPanel).setToggleIconsEnabled(false);

    innerPanel.setContent(getPanelGrid());

    setCompositionRoot(innerPanel);
  }

  private GridLayout getPanelGrid() {
    GridLayout panelGrid = new GridLayout(2, 4);

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
    panelGrid.addComponent(new Label("Conditions"), 0, 2);

    // Actions label
    panelGrid.addComponent(new Label("Actions"), 1, 2);

    // ROW 4


    return panelGrid;
  }

  private HorizontalLayout getConditionLayoutFromConditionDto() {
    return new HorizontalLayout();
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
