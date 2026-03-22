package com.inspire.user.exception;

import com.inspire.common.core.exception.BaseException;
import com.inspire.common.core.exception.ErrorCode;

public class UserException extends BaseException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
