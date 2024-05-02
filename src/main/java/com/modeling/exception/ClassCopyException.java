package com.modeling.exception;


import com.modeling.utils.ErrorCode;

public class ClassCopyException extends BusinessException {
    public ClassCopyException() {
        super(ErrorCode.CLASS_COPY_EXCEPTION);
    }
}
