package com.ps.kor.controller;

import com.ps.kor.business.AuthenticationLogic;
import com.ps.kor.business.util.message.BusinessMessage;
import com.ps.kor.controller.response.ResponseEntityFactory;
import com.ps.kor.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication endpoints.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  @Autowired
  private AuthenticationLogic authLogic;

  @PostMapping("/signup")
  public ResponseEntity signup(@RequestBody User user) {
    BusinessMessage message = authLogic.signup(user);

    return ResponseEntityFactory.createResponseFromBusinessMessage(message);
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody User user) {
    BusinessMessage message = authLogic.login(user);

    return ResponseEntityFactory.createResponseFromBusinessMessage(message);
  }

  @PostMapping("/logout")
  public ResponseEntity logout(@RequestHeader("authorization") String token) {
    BusinessMessage message = authLogic.logout(token);

    return ResponseEntityFactory.createResponseFromBusinessMessage(message);
  }
}
