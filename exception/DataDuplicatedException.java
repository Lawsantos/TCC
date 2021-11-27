package io.github.cwireset.tcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataDuplicatedException extends Exception {
    public DataDuplicatedException(String campo, String campo2, String parametro) {
        super(String.format("Já existe um recurso do tipo %s com %s com o valor '%s'.", campo, campo2, parametro));
    }
}
