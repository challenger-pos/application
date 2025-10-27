package com.fiap.core.exception;

public abstract class DomainException extends Exception {
  private final String code;

  protected DomainException(String message, String code) {
    super(message);
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
