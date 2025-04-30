package br.com.devcoelho.taskboard.exception;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", OffsetDateTime.now());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Bad Request");
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", OffsetDateTime.now());
    body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    body.put("error", "Internal Server Error");
    body.put("message", "An unexpected error occurred");

    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleResourceNotFoundException(Exception ex, WebRequest request) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", OffsetDateTime.now());
    body.put("status", HttpStatus.NOT_FOUND.value());
    body.put("error", "Not Found:404");
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
