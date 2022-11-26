package ru.yasha.webchat.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yasha.webchat.dto.ErrorDto;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(UsernameAlreadyInUseException.class)
    public ResponseEntity<ErrorDto> handleException(UsernameAlreadyInUseException e) {
        return ResponseEntity
                .badRequest()
                .body(ErrorDto.of(e.getMessage()));
    }
}
