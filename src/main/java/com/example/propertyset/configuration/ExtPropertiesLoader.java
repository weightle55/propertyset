package com.example.propertyset.configuration;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class ExtPropertiesLoader implements EnvironmentAware {

  private static final String EXTRA_YML_PATH = "extra.yml";

  private ConfigurableEnvironment environment;

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = (ConfigurableEnvironment) environment;

    Resource resource = new ClassPathResource(EXTRA_YML_PATH);

    YamlPropertySourceLoader loader = new YamlPropertySourceLoader();

    try {
      PropertySource<?> yamlProperties = loader.load("extra", resource).get(0);
      this.environment.getPropertySources().addLast(yamlProperties);
    } catch (IOException e) {
      throw new RuntimeException(EXTRA_YML_PATH + " load error", e);
    }
  }
}
