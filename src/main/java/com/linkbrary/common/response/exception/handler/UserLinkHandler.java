package com.linkbrary.common.response.exception.handler;

import com.linkbrary.common.response.code.BaseErrorCode;
import com.linkbrary.common.response.exception.GeneralException;

public class UserLinkHandler extends GeneralException {
    public UserLinkHandler(BaseErrorCode code) {
        super(code);
    }
}
