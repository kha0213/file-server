package com.file.server.api.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class CustomResponseEntity<T> extends ResponseEntity<T> {
    public CustomResponseEntity(HttpStatus status) {
        super(status);
    }

    public CustomResponseEntity(T body, HttpStatus status) {
        super(body, status);
    }

    public CustomResponseEntity(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    public CustomResponseEntity(T body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }
}
