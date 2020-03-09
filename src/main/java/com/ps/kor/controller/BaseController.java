package com.ps.kor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class BaseController {

  @GetMapping("/ping")
  public String ping() {
    return "pong";
  }
}
