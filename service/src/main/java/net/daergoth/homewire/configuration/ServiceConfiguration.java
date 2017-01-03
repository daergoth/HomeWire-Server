package net.daergoth.homewire.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {


  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
