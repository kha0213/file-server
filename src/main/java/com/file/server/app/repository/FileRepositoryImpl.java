package com.file.server.app.repository;

import com.file.server.app.entity.File;
import com.file.server.app.entity.query.FileSearch;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

import static com.file.server.app.entity.QFile.file;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class FileRepositoryImpl implements FileCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<File> findFiles(FileSearch search, Pageable pageable) {
        JPAQuery<File> query = queryFactory
                .selectFrom(file)
                .where(
                        allCondSearch(search)
                )
                .orderBy(file.createBy.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, () -> this.totalCount(search) );
    }
    
    private int totalCount(FileSearch search){
        return queryFactory
                .select(
                        file.count())
                .from(file)
                .where(
                        allCondSearch(search)
                )
                .fetch().size();
    }

    private BooleanExpression containsUploadNm(String uploadNm) {
        return hasText(uploadNm) ? file.uploadNm.contains(uploadNm) : null;
    }

    private BooleanExpression eqExtenstion(String extension) {
        return hasText(extension) ? file.extensions.eq(extension) : null;
    }

    private BooleanExpression minSize(Long size) {
        return isPositive(size)? file.size.goe(size) : null;
    }

    private BooleanExpression maxSize(Long size) {
        return isPositive(size)? file.size.loe(size) : null;
    }

    private BooleanExpression betweenSize(Long minSize, Long maxSize) {
        return isPositive(minSize, maxSize)? file.size.between(minSize, maxSize) : null;
    }

    /**
     * @param search 해당 객체의 모든 조건 검색
     */
    private BooleanBuilder allCondSearch(FileSearch search) {
        BooleanBuilder builder = new BooleanBuilder();
        if (hasText(search.getFileName())) {
            builder.and(file.uploadNm.contains(search.getFileName()));
        }
        if (isPositive(search.getMinSize())) {
            builder.and(file.size.goe(search.getMinSize()));
        }
        if (isPositive(search.getMaxSize())) {
            builder.and(file.size.loe(search.getMaxSize()));
        }
        if (search.getFileType() != null) {
            builder.and(file.fileType.eq(search.getFileType()));
        }
        if (hasText(search.getExtension())) {
            builder.and(file.extensions.eq(search.getExtension()));
        }
        if (search.getMinDate() != null) {
            builder.and(file.createdDate.after(search.getMinDate()));
        }
        if (search.getMaxDate() != null) {
            builder.and(file.createdDate.before(search.getMaxDate()));
        }
        return builder;
    }

    private boolean isPositive(Long num) {
        return num != null && num > 0;
    }

    private boolean isPositive(Long... nums) {
        return Arrays.stream(nums).allMatch(this::isPositive);
    }
}
