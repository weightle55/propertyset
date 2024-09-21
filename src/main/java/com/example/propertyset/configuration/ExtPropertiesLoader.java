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

  private static final String EXTRA_YML_PATH = "extra.yml";

  private ConfigurableEnvironment environment;

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = (ConfigurableEnvironment) environment;

    loadYamlProperties(EXTRA_YML_PATH);
  }

  private void loadYamlProperties(String resourcePath) {
    try {
      Resource resource = new ClassPathResource(resourcePath);
      YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
      // 여러 섹션이 있는 YAML 파일을 로드
      List<PropertySource<?>> yamlProperties = loader.load(resourcePath, resource);

      //profile 가져오기
      List<String> activeProfiles = Arrays.stream(environment.getActiveProfiles()).toList();
      Map<String, Object> mergedProperties = new HashMap<>();
      for (PropertySource<?> propertySource : yamlProperties) {
        //기본 섹션 추가
        if (propertySource.getProperty("on-profile") == null) {
          mergedProperties.putAll((Map<String,Object>) propertySource.getSource());
        }
        else if (activeProfiles.contains(propertySource.getProperty("on-profile"))) {
          mergedProperties.putAll((Map<String,Object>) propertySource.getSource());
        }
      }

      this.environment.getPropertySources().addLast(new MapPropertySource("extraProperties",mergedProperties));

      for (String profile : activeProfiles) {
        System.out.println(profile);
      }
    } catch (IOException e) {
      throw new RuntimeException(resourcePath + " load error", e);
    }
  }
}
