package io.github.cwireset.tcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NumberLimitException extends Exception{
    public NumberLimitException(Integer numero, String primeiroCampo, String segundoCampo) {
        super(String.format("Não é possivel realizar uma reserva com menos de %s %s para imóveis do tipo %s", numero, primeiroCampo, segundoCampo));
    }
}
