package com.file.server.app.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileSearch {
    // 파일 명
    private String fileName;
    // 파일 최소 사이즈
    private Long minSize;
    // 파일 최대 사이즈
    private Long maxSize;
    // 파일 타입
    private FileType fileType;
    // 파일 확장자
    private String extension;
    // 검색 시작일 (파일 등록일 검색)
    private LocalDateTime minDate;
    // 검색 종료일 (파일 등록일 검색)
    private LocalDateTime maxDate;
    // 파일 생성 유저
    private String createBy;


}
