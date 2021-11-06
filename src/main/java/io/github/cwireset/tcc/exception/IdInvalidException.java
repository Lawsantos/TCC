package io.github.cwireset.tcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IdInvalidException extends Exception{
    public IdInvalidException(Long id) {
        super(String.format("Nenhum(a) Usuario com Id com o valor '%s' foi encontrado.", id));
    }
}
