package com.linkbrary.common.response.exception.handler;

import com.linkbrary.common.response.code.BaseErrorCode;
import com.linkbrary.common.response.exception.GeneralException;

public class ReminderHandler  extends GeneralException {
    public ReminderHandler(BaseErrorCode code) {
        super(code);
    }
}
