package com.ps.kor.business.util;

public class BusinessMessage {

  private Object data;

  private BusinessMesageType type;

  public BusinessMessage(BusinessMesageType type) {
    this(null, type);
  }

  public BusinessMessage(Object data, BusinessMesageType type) {
    this.data = data;
    this.type = type;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public BusinessMesageType getType() {
    return type;
  }

  public void setType(BusinessMesageType type) {
    this.type = type;
  }
}
