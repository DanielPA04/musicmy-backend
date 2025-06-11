package com.musicmy.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.musicmy.bean.ErrorBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorBean> handleException(Exception ex) {
        ErrorBean oErrorBean = new ErrorBean();
        oErrorBean.setMessage(ex.getMessage());
        oErrorBean.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        oErrorBean.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(oErrorBean, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorBean> ResourceNotFoundException(ResourceNotFoundException exception) {
        ErrorBean oErrorBean = new ErrorBean();
        oErrorBean.setMessage(exception.getMessage());
        oErrorBean.setStatus(HttpStatus.NOT_FOUND.value());
        oErrorBean.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(oErrorBean, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorBean> UnauthorizedAccessException(UnauthorizedAccessException exception) {
        ErrorBean oErrorBean = new ErrorBean();
        oErrorBean.setMessage(exception.getMessage());
        oErrorBean.setStatus(HttpStatus.UNAUTHORIZED.value());
        oErrorBean.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(oErrorBean, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = NoSessionException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorBean> NoSessionException(NoSessionException exception) {
        ErrorBean oErrorBean = new ErrorBean();
        oErrorBean.setMessage(exception.getMessage());
        oErrorBean.setStatus(HttpStatus.UNAUTHORIZED.value());
        oErrorBean.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(oErrorBean, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = VerifyException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorBean> VerifyException(VerifyException exception) {
        ErrorBean oErrorBean = new ErrorBean();
        oErrorBean.setMessage(exception.getMessage());
        oErrorBean.setStatus(HttpStatus.UNAUTHORIZED.value());
        oErrorBean.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(oErrorBean, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = ErrorChangingPassword.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorBean> ErrorChangingPassword(ErrorChangingPassword exception) {
        ErrorBean oErrorBean = new ErrorBean();
        oErrorBean.setMessage(exception.getMessage());
        oErrorBean.setStatus(HttpStatus.UNAUTHORIZED.value());
        oErrorBean.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(oErrorBean, HttpStatus.UNAUTHORIZED);
    }

      @ExceptionHandler(value = ErrorRegistering.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorBean> ErrorRegistering(ErrorRegistering exception) {
        ErrorBean oErrorBean = new ErrorBean();
        oErrorBean.setMessage(exception.getMessage());
        oErrorBean.setStatus(HttpStatus.UNAUTHORIZED.value());
        oErrorBean.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(oErrorBean, HttpStatus.UNAUTHORIZED);
    }

}
