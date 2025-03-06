package kpu.cybersecurity.training.util.exception;

import kpu.cybersecurity.training.domain.RestResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value ={
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            Exception.class
    })
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(Exception e) {
        RestResponse<Object> res = new RestResponse<Object>();

        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(e.getClass().getName());
        res.setMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException e) {
        BindingResult rs = e.getBindingResult();

        final List<FieldError> fieldErrors = rs.getFieldErrors();

        RestResponse<Object> res = new RestResponse<Object>();

        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(e.getBody().getDetail());

        List<String> errors = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();

        res.setMessage(errors.size() > 1? errors : errors.getFirst());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
   }
}
