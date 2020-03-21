package com.ps.kor.controller.response;

import java.util.LinkedList;
import java.util.List;

public class ResponseWrapper {

  private Object data;

  private List<String> messages;

  private List<String> errorMessages;

  public ResponseWrapper() {
  }

  public ResponseWrapper(Object data) {
    this.data = data;
    this.messages = new LinkedList<>();
    this.errorMessages = new LinkedList<>();
  }

  public void addMessage(String message) {
    this.messages.add(message);
  }

  public void addErrorMessage(String errorMessage) {
    this.errorMessages.add(errorMessage);
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public List<String> getMessages() {
    return messages;
  }

  public void setMessages(List<String> messages) {
    this.messages = messages;
  }

  public List<String> getErrorMessages() {
    return errorMessages;
  }

  public void setErrorMessages(List<String> errorMessages) {
    this.errorMessages = errorMessages;
  }
}
