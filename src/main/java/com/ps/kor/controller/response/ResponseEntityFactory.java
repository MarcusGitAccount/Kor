package com.ps.kor.controller.response;

import com.ps.kor.business.util.BusinessMesageType;
import com.ps.kor.business.util.BusinessMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseEntityFactory {

  private static Map<BusinessMesageType, HttpStatus> businessTypeToHtppStatus = new HashMap<>();

  static {
    businessTypeToHtppStatus.put(BusinessMesageType.BUDGET_CREATION_FAIL,           HttpStatus.BAD_REQUEST);
    businessTypeToHtppStatus.put(BusinessMesageType.USER_NOT_FOUND_AUTH,            HttpStatus.UNAUTHORIZED);
    businessTypeToHtppStatus.put(BusinessMesageType.UNAUTHORIZED_ROLE_CREATION,     HttpStatus.UNAUTHORIZED);
    businessTypeToHtppStatus.put(BusinessMesageType.BUDGET_CREATION_SUCCESS,        HttpStatus.CREATED);
    businessTypeToHtppStatus.put(BusinessMesageType.ROLE_CREATION_SUCCESS,          HttpStatus.CREATED);
    businessTypeToHtppStatus.put(BusinessMesageType.SIGNUP_SUCCESS,                 HttpStatus.CREATED);
    businessTypeToHtppStatus.put(BusinessMesageType.USER_NOT_FOUND,                 HttpStatus.NOT_FOUND);
    businessTypeToHtppStatus.put(BusinessMesageType.BUDGET_NOT_FOUND,               HttpStatus.NOT_FOUND);
    businessTypeToHtppStatus.put(BusinessMesageType.USER_NOT_PART_OF_BUDGET,        HttpStatus.NOT_FOUND);
  }

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

  public static ResponseEntity<ResponseWrapper> createResponseFromBusinessMessage(BusinessMessage businessMessage) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    BusinessMesageType type = businessMessage.getType();

    if (!businessTypeToHtppStatus.containsKey(businessMessage.getType())) {
      if (!type.isError()) {
        status = HttpStatus.OK;
      }
    }
    else {
      status = businessTypeToHtppStatus.get(businessMessage.getType());
    }

    if (type.isError()) {
      return buildErrorResponse(businessMessage.getData(), type.getDetails(), status);
    }

    return buildSuccesResponse(businessMessage.getData(), type.getDetails(), status);
  }
}
