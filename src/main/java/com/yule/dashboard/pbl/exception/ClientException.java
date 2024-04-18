package com.yule.dashboard.pbl.exception;

import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {
    private final ExceptionCause because;

    public ClientException(ExceptionCause because) {
        this.because = because;
    }
}
