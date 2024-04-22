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
    AUTH_CODE_IS_NOT_MATCHES(492, "need to retry sign up"),
    RETRY_SIGN_UP(491, "need to retry sign up"),
    TOKEN_IS_EXPIRED(490, "token is expired"),
    PRIMARY_KEY_IS_NOT_VALID(489, "primary key is not valid check again"),
    CAN_NOT_BE_ALL_NULL(488, "can not be null all arguments"),
    PW_LENGTH_ERROR(487, "password legnth error"),
    ID_LENGTH_ERROR(486, "id legnth error"),
    NICK_LENGTH_ERROR(485, "nick legnth error"),
    REQUEST_VALUE_IS_NOT_VALID(484, "request value is not valid")

    ;
    private final int code;
    private final String msg;

    ExceptionCause(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
