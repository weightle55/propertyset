package com.example.propertyset.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TestService {

  @Value("${ext.test.value}")
  private String testValue;

  public String getTestValue() {
    return testValue;
  }
}
