package com.file.server.app.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class NoSuchFileException extends Exception {
    private Long fileId;
    public NoSuchFileException(Long fileId) {
        this.fileId = fileId;
    }
}
