package com.example.propertyset.controller;

import com.example.propertyset.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {

  private final TestService testService;

  @GetMapping("test/value")
  public String getTestValue() {
    return testService.getTestValue();
  }
}
