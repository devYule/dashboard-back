package com.yule.dashboard.pbl.exception;

import lombok.Getter;

@Getter
public class ServerException extends RuntimeException {
    private final ExceptionCause because;

    public ServerException() {
        this.because = ExceptionCause.SERVER_ERROR;
    }

    public ServerException(Throwable cause) {
        super(cause);
        this.because = ExceptionCause.SERVER_ERROR;
    }
}
