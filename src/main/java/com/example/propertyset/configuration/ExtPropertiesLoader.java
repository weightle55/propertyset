package com.example.propertyset.configuration;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ExtPropertiesLoader implements EnvironmentAware {

  private static final String YML_FILE_NAME = "extra";

  private ConfigurableEnvironment environment;

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = (ConfigurableEnvironment) environment;

    loadYamlFileByProfile();
  }

  private void loadYamlFileByProfile() {
    try {
      //defaultResource 가져오기
      Resource defaultResource = new ClassPathResource(YML_FILE_NAME + ".yml");
      YamlPropertySourceLoader loader = new YamlPropertySourceLoader();

      // default yml 읽기
      List<PropertySource<?>> yamlProperties = loader.load("extra", defaultResource);
      Map<String, Object> mergedProperties = new HashMap<>();
      // extra.yml 파일이 있다면 초기화
      if (!yamlProperties.isEmpty()) {
        mergedProperties.putAll((Map<String, Object>) yamlProperties.get(0).getSource());
      }

      //Active Profile 가져오기
      String[] activeProfiles = environment.getActiveProfiles();
      for (String profile : activeProfiles) {
        //extra-profile.yml 읽기
        Resource profileResource = new ClassPathResource(YML_FILE_NAME + "-" + profile + ".yml");
        List<PropertySource<?>> profileYamlProperties = loader.load("extra", profileResource);

        // profile 설정이 되어있고, profile file이 있으면, property Map의 같은 키값 덮기
        if(!profileYamlProperties.isEmpty()) {
          mergedProperties.putAll((Map<String, Object>) profileYamlProperties.get(0).getSource());
        }
      }
      this.environment.getPropertySources().addLast(new MapPropertySource("extraProperties", mergedProperties));
    } catch (IOException e) {
      throw new RuntimeException(YML_FILE_NAME + " load error", e);
    }
  }
}
