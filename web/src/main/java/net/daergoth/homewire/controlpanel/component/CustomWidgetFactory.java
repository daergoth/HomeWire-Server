package net.daergoth.homewire.controlpanel.component;


import net.daergoth.homewire.setup.DeviceDTO;

public interface CustomWidgetFactory {

  RefreshableWidget createWidget(DeviceDTO device);

}
