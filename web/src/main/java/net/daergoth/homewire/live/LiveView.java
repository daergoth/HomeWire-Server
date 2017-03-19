package net.daergoth.homewire.live;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import net.daergoth.homewire.BaseUI;
import net.daergoth.homewire.live.component.CustomWidgetFactory;
import net.daergoth.homewire.live.component.CustomWidgetRepository;
import net.daergoth.homewire.live.component.RefreshableWidget;
import net.daergoth.homewire.setup.DeviceSetupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

@SpringView(name = LiveView.VIEW_NAME, ui = BaseUI.class)
@Title("Live - HomeWire")
public class LiveView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "live";

  private CssLayout dashboard;

  private Map<String, RefreshableWidget> widgetMap;

  private Map<String, Panel> panelMap;

  @Autowired
  private LiveDataService liveDataService;

  @Autowired
  private CustomWidgetRepository customWidgetRepository;

  @Autowired
  private DeviceSetupService deviceSetupService;

  @Autowired
  private BaseUI ui;

  @PostConstruct
  void init() {
    Label header = new Label("Live");
    header.setStyleName(ValoTheme.LABEL_H1);
    addComponent(header);

    dashboard = new CssLayout();
    dashboard.setSizeFull();

    widgetMap = new HashMap<>();

    panelMap = new HashMap<>();

    generateDashboard();

    addComponent(dashboard);

    ui.setPollInterval(2000);
    ui.addPollListener(event -> {
      generateDashboard();
    });
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {
    // This view is constructed in the init() method()
  }

  private void generateDashboard() {
    for (LiveDataDTO liveData : liveDataService.getCurrentDeviceData()) {

      if (widgetMap.containsKey(liveData.getType() + liveData.getId())) {
        RefreshableWidget refreshableWidget = widgetMap.get(liveData.getType() + liveData.getId());

        if (liveData.getValue().getClass().equals(refreshableWidget.getRefreshType())) {
          refreshableWidget.refresh(liveData.getValue());
        }

      } else {
        RefreshableWidget refreshableWidget = getWidget(liveData);

        widgetMap.put(liveData.getType() + liveData.getId(), refreshableWidget);

        if (panelMap.containsKey(liveData.getType())) {
          HorizontalLayout innerLayout =
              (HorizontalLayout) panelMap.get(liveData.getType()).getContent();

          innerLayout.addComponent(refreshableWidget);

        } else {
          HorizontalLayout innerLayout = new HorizontalLayout(refreshableWidget);

          Panel typePanel = new Panel(
              liveData.getType().substring(0, 1).toUpperCase() + liveData.getType().substring(1),
              innerLayout
          );

          panelMap.put(liveData.getType(), typePanel);
        }

      }
    }

    panelMap.values().stream().sorted(Comparator.comparing(Panel::getCaption))
        .forEach(panel -> dashboard.addComponent(panel));
  }

  private RefreshableWidget getWidget(LiveDataDTO liveDataDTO) {
    CustomWidgetFactory widgetFactory =
        customWidgetRepository.getWidgetFactory(liveDataDTO.getType());

    if (widgetFactory != null) {
      RefreshableWidget refreshableWidget =
          widgetFactory.createWidget(deviceSetupService
              .getDeviceNameByIdAndType(liveDataDTO.getId(), liveDataDTO.getType()));

      if (liveDataDTO.getValue().getClass().equals(refreshableWidget.getRefreshType())) {
        refreshableWidget.refresh(liveDataDTO.getValue());
      }

      return refreshableWidget;
    } else {
      return new RefreshableWidget() {
        @Override
        public void refresh(Object value) {}

        @Override
        public Class getRefreshType() {
          return null;
        }
      };
    }

  }
}