package com.file.server.api.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public class ApiException extends Exception {
    private final ErrorCode errorCode;

}
