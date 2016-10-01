package com.waes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by aandra1 on 30/09/16.
 */


@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class PayloadExceedMaxNumberException extends RuntimeException {

  public PayloadExceedMaxNumberException(String message) {
    super(message);
  }
}
