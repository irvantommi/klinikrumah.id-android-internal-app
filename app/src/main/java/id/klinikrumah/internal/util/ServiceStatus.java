package id.klinikrumah.internal.util;

public enum ServiceStatus {
    SUCCESS,
    SUCCESS_ERROR,
    ERROR_PARSING,
    ERROR_NETWORK, // 0
    UNAUTHORIZED, // 401
    ERROR_GENERAL,
    TIME_OUT
}
//General Error
//404 Not Found
//500 Internal Server Error
//502 Bad Gateway
//504 Gateway Timeout
//400 Bad Request
//403 Forbidden