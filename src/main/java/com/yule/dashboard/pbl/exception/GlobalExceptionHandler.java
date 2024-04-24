package com.yule.dashboard.pbl.exception;

import com.yule.dashboard.pbl.exception.model.ExceptionData;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler
    public ExceptionData illegalArgumentException(MethodArgumentNotValidException e) {
        log.error("error", e);
        ExceptionCause ec = ExceptionCause.getByMsg(e.getMessage());
        return new ExceptionData(ec.getCode(), ec.getMsg());
    }

    @ExceptionHandler
    public ExceptionData clientException(ClientException e) {
        log.error("error", e);
        return new ExceptionData(e.getBecause().getCode(), e.getBecause().getMsg());
    }

    @ExceptionHandler
    public ExceptionData serverException(ServerException e) {
        log.error("error", e);
        return new ExceptionData(e.getBecause().getCode(), e.getBecause().getMsg());
    }

    @ExceptionHandler
    public ExceptionData runtimeException(RuntimeException e) {
        log.error("error", e);
        return new ExceptionData(ExceptionCause.SERVER_ERROR.getCode(), ExceptionCause.SERVER_ERROR.getMsg());
    }

}
