package com.file.server.app.repository;

import com.file.server.app.entity.File;
import com.file.server.app.entity.query.FileSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FileCustomRepository {
    Page<File> findFiles(FileSearch search, Pageable pageable);
}
