package com.linkbrary.common.response.exception.handler;

import com.linkbrary.common.response.exception.GeneralException;
import com.linkbrary.common.response.code.BaseErrorCode;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode code) {
        super(code);
    }
}
