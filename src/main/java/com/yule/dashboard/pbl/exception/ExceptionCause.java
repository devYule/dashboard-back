package com.yule.dashboard.pbl.exception;

import lombok.Getter;

import java.util.Arrays;


@Getter
public enum ExceptionCause {
    SERVER_ERROR(500, ExceptionMessages.SERVER_ERROR),
    ID_NOT_EXISTS(499, ExceptionMessages.ID_NOT_EXISTS),
    PW_NOT_EXISTS(498, ExceptionMessages.PW_NOT_EXISTS),
    PW_NOT_MATCHES(497, ExceptionMessages.PW_NOT_MATCHES),
    ID_IS_ALREADY_EXISTS(496, ExceptionMessages.ID_IS_ALREADY_EXISTS),
    NICK_IS_ALREADY_EXISTS(495, ExceptionMessages.NICK_IS_ALREADY_EXISTS),
    ID_AND_NICK_IS_ALREADY_EXISTS(494, ExceptionMessages.ID_AND_NICK_IS_ALREADY_EXISTS),
    MAIL_IS_ALREADY_EXISTS(493, ExceptionMessages.MAIL_IS_ALREADY_EXISTS),
    AUTH_CODE_IS_NOT_MATCHES(492, ExceptionMessages.AUTH_CODE_IS_NOT_MATCHES),
    RETRY_SIGN_UP(491, ExceptionMessages.RETRY_SIGN_UP),
    TOKEN_IS_EXPIRED(490, ExceptionMessages.TOKEN_IS_EXPIRED),
    PRIMARY_KEY_IS_NOT_VALID(489, ExceptionMessages.PRIMARY_KEY_IS_NOT_VALID),
    CAN_NOT_BE_ALL_NULL(488, ExceptionMessages.CAN_NOT_BE_ALL_NULL),
    PW_LENGTH_ERROR(487, ExceptionMessages.PW_LENGTH_ERROR),
    ID_LENGTH_ERROR(486, ExceptionMessages.ID_LENGTH_ERROR),
    NICK_LENGTH_ERROR(485, ExceptionMessages.NICK_LENGTH_ERROR),
    REQUEST_VALUE_IS_NOT_VALID(484, ExceptionMessages.REQUEST_VALUE_IS_NOT_VALID),
    REQUEST_VALUE_MUST_BE_NOT_BLANK(483, ExceptionMessages.REQUEST_VALUE_MUST_BE_NOT_BLANK),
    MUST_MOVE_TO_HOME(501, ExceptionMessages.MUST_MOVE_TO_HOME),
    FILE_IS_NOT_EXISTS(482, ExceptionMessages.FILE_IS_NOT_EXISTS),
    REQUEST_VALUE_RANGE_ERROR(481, ExceptionMessages.REQUEST_VALUE_RANGE_ERROR),
    RETRY_SIGN_IN(480, ExceptionMessages.RETRY_SIGN_IN),
    SITE_IS_EMPTY(479, ExceptionMessages.SITE_IS_EMPTY),

    ;
    private final int code;
    private final String msg;

    ExceptionCause(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ExceptionCause getByMsg(String msg) {
        return Arrays.stream(ExceptionCause.values()).filter(ec -> ec.getMsg().equals(msg) || msg.contains(ec.getMsg())).findFirst()
                .orElseThrow(ServerException::new);
    }
}
