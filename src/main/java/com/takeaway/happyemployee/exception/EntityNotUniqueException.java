package com.takeaway.happyemployee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityNotUniqueException extends RuntimeException {

    public EntityNotUniqueException(String reason) {
        super(reason);
    }
}
