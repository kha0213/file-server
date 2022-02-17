package com.file.server.api.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SuccessResponse<T> {
    private final boolean success = true;

    private T data;

    public SuccessResponse(T data) {
        this.data = data;
    }
}
