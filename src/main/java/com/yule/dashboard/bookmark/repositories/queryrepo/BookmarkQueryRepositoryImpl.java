package com.yule.dashboard.bookmark.repositories.queryrepo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class BookmarkQueryRepositoryImpl implements BookmarkQueryRepository {

    private final JPAQueryFactory query;


//    @Override
//    public List<Bookmark> findByUrlPathIn(List<String> totalUrls) {
//        return query.select(bookMark)
//                .from(bookMark)
//                .where(bookMark.url.in(totalUrls))
//                .fetch();
//
//    }
}
