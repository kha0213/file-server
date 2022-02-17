package com.file.server.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static com.file.server.api.common.ErrorCode.INTERNAL_SERVER_ERROR;
import static java.util.stream.Collectors.toList;

/**
 * 공통으로 에러일 경우 리턴이다.
 */

@Getter
public class ErrorResponse {
    private final boolean success = false;
    // 에러에 대한 메세지 이다.
    private String message;
    // HTTP 상태코드이다.
    private int status;
    // 어떤 필드에서 에러 났는지 알려주는 원인이다.
    private List<FieldError> fieldErrors = new ArrayList<>();
    // 비즈니스에서 사용할 에러 코드이다.
    private String code;

    private ErrorResponse(ErrorCode errorCode) {
        this(errorCode, null);
    }

    private ErrorResponse(ErrorCode errorCode, BindingResult result) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        if (result != null) {
            this.fieldErrors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::new)
                    .collect(toList());
        }
    }

    private ErrorResponse(Exception e) {
        this.message = e.getMessage();
        this.status = INTERNAL_SERVER_ERROR.getStatus();
        this.code = INTERNAL_SERVER_ERROR.getCode();
    }

    @Getter
    @AllArgsConstructor
    static class FieldError {
        private String field;
        private Object value;
        private String reason;

        public FieldError(org.springframework.validation.FieldError fieldError) {
            this.field = fieldError.getField();
            this.value = fieldError.getRejectedValue();
            this.reason = fieldError.getDefaultMessage();
        }
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }
    public static ErrorResponse of(Exception e) {
        return new ErrorResponse(e);
    }

    public static ErrorResponse of(ErrorCode errorCode, BindingResult result) {
        return new ErrorResponse(errorCode, result);
    }
}
