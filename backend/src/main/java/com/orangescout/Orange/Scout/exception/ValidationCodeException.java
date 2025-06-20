package com.orangescout.Orange.Scout.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationCodeException extends RuntimeException {

  public ValidationCodeException(String message) {
    super(message);
  }

  public ValidationCodeException(String message, Throwable cause) {
    super(message, cause);
  }
}