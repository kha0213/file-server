# file server

REST API를 통해 쉽게 파일을 Upload, download, list 조회 할 수 있는 파일 서버 시스템입니다.

[![License](https://img.shields.io/badge/springboot-2.6.3-yellowgreen)](https://img.shields.io/badge/springboot-2.6.3-yellowgreen)
[![Java11](https://img.shields.io/badge/java-11-yellowgreen)](https://img.shields.io/badge/java-11-yellowgreen)
[![Gradle](https://img.shields.io/badge/gradle-v7.4-yellowgreen)](https://img.shields.io/badge/gradle-v7.4-yellowgreen)
[![querydsl](https://img.shields.io/badge/querydsl-5.0.0-yellowgreen)](https://img.shields.io/badge/querydsl-5.0.0-yellowgreen)
![last commit](https://img.shields.io/github/last-commit/beygee/survive)

## 시스템 필요 조건
* [JDK 11](https://jdk.java.net/11/)
* [Gradle 7.4](https://gradle.org/install/)

## 구현된 기능
* 파일 업로드, 다운로드 (여러 파일 가능)
* 파일 검색, 리스트 조회, 페이징
* 파일 삭제

## 업데이트 예정 기능
* 관리자 화면
* 파일 보안 시스템

## API

#### Get list of files
* __GET__ http://localhost:8812/file/list 파일 리스트 조회
  ``curl --location --request GET 'http://localhost:8812/file/list'``
  
#### Get file
* __GET__ http://localhost:8812/file/{fileId} 단 건 파일 조회   
  ``curl --location --request GET 'http://localhost:8812/file/${fileId}'``

#### Upload files
**[필수] name = file 로 파일 1개 이상 첨부**  
* __POST__ http://localhost:8812/file/upload 파일 한개 이상 업로드  
  ``curl --location --request POST 'http://localhost:8080/file/upload' \
  --form 'file=@"/C:/test/TEST1.png"' \
  --form 'file=@"/C:/test/TEST2.png"'``

#### Delete file
* __POST__ http://localhost:8812/file/delete/{fileId} 단 건 파일 삭제  
  ``curl --location --request POST 'http://localhost:8812/file/delete/${fileId}'``

#### Delete files
**[필수] name = fileId 로 파일Id 1개 이상 보내야 함**
* __POST__ http://localhost:8812/file/delete/{fileId} 한 건 이상 파일 삭제  
  ``curl --location --request POST 'http://localhost:8080/file/delete' \
  --form 'fileId="1"' \
  --form 'fileId="2"'``
