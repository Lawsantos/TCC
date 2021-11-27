package io.github.cwireset.tcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ConditiondInvalidException extends Exception{
    public ConditiondInvalidException(String s) {
        super(s);
    }
}
