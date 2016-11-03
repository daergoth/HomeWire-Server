package net.daergoth.homewire;

import net.daergoth.homewire.SensorDataDTO;
import net.daergoth.homewire.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

  @Autowired
  private SensorDataService sensorDataService;

  @RequestMapping("/")
  public List<SensorDataDTO<?>> home() {
    return sensorDataService.getSensorData();
  }

}
