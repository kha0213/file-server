package com.file.server.api.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Result<T> {
    private final boolean success = true;

    private T data;

    public Result(T data) {
        this.data = data;
    }

    /**
     * data 없이 기본 성공만 전해주는 리턴이다.
     */
    public static Result<Object> success() {
        return new Result<>(null);
    }
}
