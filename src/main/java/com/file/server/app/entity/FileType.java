package com.file.server.app.entity;

import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Set;

@Getter
public enum FileType {
    IMAGE(Set.of("jpg","jpeg","png","bmp")),
    VIDEO(Set.of("mp4","avi","wmv")),
    ATTACH(Set.of());

    private final Set<String> extensions;

    FileType(Set<String> extensions) {
        this.extensions = extensions;
    }

    public static FileType getFileType(String extension) {
        return Arrays.stream(FileType.values())
                .filter(fileType -> fileType.hasExtensions(extension))
                .findAny()
                .orElse(FileType.ATTACH);
    }

    public boolean hasExtensions(String extension) {
        if(!StringUtils.hasText(extension)) return false;
        return extensions.contains(extension.toLowerCase());
    }
}
