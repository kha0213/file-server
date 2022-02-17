package com.file.server.app.repository;

import com.file.server.app.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long>, FileCustomRepository {
    Optional<File> findBySizeEquals(Long size);
}
