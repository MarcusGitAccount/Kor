package com.ps.kor.business.util.message;

public class BusinessMessage<T> {

  private T data;

  private BusinessMesageType type;

  public BusinessMessage(BusinessMesageType type) {
    this(null, type);
  }

  public BusinessMessage(T data, BusinessMesageType type) {
    this.data = data;
    this.type = type;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public BusinessMesageType getType() {
    return type;
  }

  public void setType(BusinessMesageType type) {
    this.type = type;
  }
}
