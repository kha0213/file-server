package com.file.server.app.exception;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class NoSuchFileException extends Exception {
    public NoSuchFileException(Long fileId) {
        log.error("No such File FileId [{}]", fileId);
    }
}
