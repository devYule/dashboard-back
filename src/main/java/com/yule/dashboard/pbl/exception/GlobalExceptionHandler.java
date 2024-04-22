package com.yule.dashboard.pbl.exception;

import com.yule.dashboard.pbl.exception.model.ExceptionData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ExceptionData clientException(ClientException e) {
        return new ExceptionData(e.getBecause().getCode(), e.getBecause().getMsg());
    }

    @ExceptionHandler
    public ExceptionData serverException(ServerException e) {
        return new ExceptionData(e.getBecause().getCode(), e.getBecause().getMsg());
    }

    @ExceptionHandler
    public ExceptionData runtimeException(RuntimeException e) {
        log.error("error", e);
        return new ExceptionData(ExceptionCause.SERVER_ERROR.getCode(), ExceptionCause.SERVER_ERROR.getMsg());
    }

}
