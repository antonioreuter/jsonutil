package com.waes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by aandra1 on 02/10/16.
 */

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class PayloadComparisonException extends RuntimeException {

  public PayloadComparisonException(String message) {
    super(message);
  }

  public PayloadComparisonException(String message, Throwable th) {
    super(message, th);
  }
}
