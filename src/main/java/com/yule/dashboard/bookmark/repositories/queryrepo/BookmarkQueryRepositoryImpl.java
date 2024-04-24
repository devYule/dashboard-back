package com.yule.dashboard.bookmark.repositories.queryrepo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.yule.dashboard.entities.QBookMark.bookMark;


@RequiredArgsConstructor
public class BookmarkQueryRepositoryImpl implements BookmarkQueryRepository {

    private final JPAQueryFactory query;


    @Override
    public List<String> findUrlByUrlPathIn(List<String> totalUrls) {
        return query.select(bookMark.url)
                .from(bookMark)
                .where(bookMark.url.in(totalUrls))
                .fetch();

    }
}
