package com.yule.dashboard.pbl.exception;

public interface ExceptionMessages {
    String SERVER_ERROR = "internal server error";
    String ID_NOT_EXISTS = "id not exists";
    String PW_NOT_EXISTS = "password not exists";
    String PW_NOT_MATCHES = "password is not matches";
    String ID_IS_ALREADY_EXISTS = "id is already exists";
    String NICK_IS_ALREADY_EXISTS = "nick is already exists";
    String ID_AND_NICK_IS_ALREADY_EXISTS = "id and nick is already exists";
    String MAIL_IS_ALREADY_EXISTS = "mail is already exists";
    String AUTH_CODE_IS_NOT_MATCHES = "code is not match";
    String RETRY_SIGN_UP = "need to retry sign up";
    String TOKEN_IS_EXPIRED = "token is expired";
    String PRIMARY_KEY_IS_NOT_VALID = "primary key is not valid check again";
    String CAN_NOT_BE_ALL_NULL = "can not be null all arguments";
    String PW_LENGTH_ERROR = "password legnth error";
    String ID_LENGTH_ERROR = "id legnth error";
    String NICK_LENGTH_ERROR = "nick legnth error";
    String REQUEST_VALUE_IS_NOT_VALID = "request value is not valid";
    //
    String REQUEST_VALUE_MUST_BE_NOT_BLANK = "request value must be not blank";
    String REQUEST_VALUE_LENGTH_MUST_BE_BETWEEN = "request value length must be between ";
    String MUST_MOVE_TO_HOME = "must move to home";

    String FILE_IS_NOT_EXISTS = "file is not exists";
    String REQUEST_VALUE_RANGE_ERROR = "request value range must be ";
    String RETRY_SIGN_IN = "retry sign up";
    String SITE_IS_EMPTY = "site is empty";
    String QUERY_ERROR = "bad search query";
    String TYPE_ERROR = "bad type";
    String ID_LANGUAGE_ERROR = "id language must be only english";
}
