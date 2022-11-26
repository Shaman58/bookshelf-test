package edu.shmonin.bookshelftest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ExceptionData> handleException(EntityNotFoundException e) {
        var data = new ExceptionData(e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionData> handleException(RuntimeException e) {
        var data = new ExceptionData(e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}