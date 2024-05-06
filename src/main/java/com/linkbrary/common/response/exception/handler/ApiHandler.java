package com.linkbrary.common.response.exception.handler;

import com.linkbrary.common.response.code.BaseErrorCode;
import com.linkbrary.common.response.exception.GeneralException;

public class ApiHandler extends GeneralException {

    public ApiHandler(BaseErrorCode code) {
        super(code);
    }
}
