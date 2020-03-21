package com.ps.kor.controller.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityFactory {

  public static ResponseEntity<ResponseWrapper> buildSuccesResponse(
      Object body,
      String message,
      HttpStatus status
  ) {
    ResponseWrapper wrapper = new ResponseWrapper(body);

    wrapper.addMessage(message);
    return ResponseEntity.status(status).body(wrapper);
  }

  public static ResponseEntity<ResponseWrapper> buildErrorResponse(
      Object body,
      String message,
      HttpStatus status
  ) {
    ResponseWrapper wrapper = new ResponseWrapper(body);

    wrapper.addErrorMessage(message);
    return ResponseEntity.status(status).body(wrapper);
  }

}
