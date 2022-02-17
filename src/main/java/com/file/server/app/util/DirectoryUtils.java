package com.file.server.app.util;


import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.io.File.separator;
import static java.io.File.separatorChar;
import static org.springframework.util.StringUtils.hasText;

public class DirectoryUtils {
    @Getter
    public enum DirDateType {
        DATE_POLICY_YYYY_MM_DD("yyyy"+separator+"MM"+separator+"dd"+separator),
        DATE_POLICY_YYYY_MM("yyyy"+separator+"MM"+separator),
        DATE_POLICY_YYYY("yyyy"+separator);

        private final String pattern;

        DirDateType(String pattern) {
            this.pattern = pattern;
        }
    }

    public static String addTodayDirectoryPath(String path) {
        return addTodayDirectoryPath(path, DirDateType.DATE_POLICY_YYYY_MM_DD);
    }

    public static String addTodayDirectoryPath(String path, DirDateType dateType) {
        String format = LocalDate.now().format(DateTimeFormatter.ofPattern(dateType.getPattern()));
        return getDirectoryPath(path, format);

    }

    public static String getDirectoryPath(String... path) {
        StringBuilder sb = new StringBuilder();

        for (String str : path) {
            if(hasText(str)) {
                sb.append(setLastChatSeparator(str));
            }
        }
        return sb.toString();
    }

    /**
     * 문자열의 마지막 문자에  File.separator 추가
     * @param str 문자열
     * @return File.separator 추가한 문자열
     */
    private static String setLastChatSeparator(@NotBlank String str) {
        char lastChar = str.charAt(str.length() - 1);

        if (lastChar == separatorChar) {
            return str;
        } else if(lastChar == '/' || lastChar == '\\') {
            return str.substring(0, str.length() - 1) + separator;
        } else {
            return str + separator;
        }
    }
}
