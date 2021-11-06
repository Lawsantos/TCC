package io.github.cwireset.tcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataDuplicatedException extends Exception {
    public DataDuplicatedException(String campo, String parametro) {
        super(String.format("Já existe um recurso do tipo Usuario com %s com o valor '%s'.", campo, parametro));
    }
}
