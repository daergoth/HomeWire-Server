package net.daergoth.homewire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HomewireAppApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext ctx = SpringApplication.run(HomewireAppApplication.class, args);
    ctx.getBean(NetworkServer.class).run();
  }
}
