package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IdInvalidException extends Exception{
    public IdInvalidException(String campo, Long id) {
        super(String.format("Nenhum(a) %s com Id com o valor '%s' foi encontrado.", campo, id));
    }
}
