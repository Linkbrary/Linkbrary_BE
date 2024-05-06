package com.linkbrary.common.response.exception.handler;

import com.linkbrary.common.response.code.BaseErrorCode;
import com.linkbrary.common.response.exception.GeneralException;

public class UserDirectoryHandler extends GeneralException {
    public UserDirectoryHandler(BaseErrorCode code) {
        super(code);
    }
}
