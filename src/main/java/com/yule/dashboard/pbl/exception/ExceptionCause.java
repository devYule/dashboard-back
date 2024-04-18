package com.yule.dashboard.pbl.exception;

import lombok.Getter;

@Getter
public enum ExceptionCause {
    SERVER_ERROR(500, "internal server error"),
    ID_NOT_EXISTS(499, "id not exists"),
    PW_NOT_EXISTS(498, "password not exists"),
    PW_NOT_MATCHES(497, "password is not matches"),
    ID_IS_ALREADY_EXISTS(496, "id is already exists"),
    NICK_IS_ALREADY_EXISTS(495, "nick is already exists"),
    ID_AND_NICK_IS_ALREADY_EXISTS(494, "id and nick is already exists"),
    MAIL_IS_ALREADY_EXISTS(493, "mail is already exists"),
    RETRY_SIGN_UP(492, "need to retry sign up")

    ;
    private final int code;
    private final String msg;

    ExceptionCause(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
