package com.yule.dashboard.bookmark.repositories.queryrepo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yule.dashboard.entities.BookMark;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.yule.dashboard.entities.QBookMark.bookMark;


@RequiredArgsConstructor
public class BookmarkQueryRepositoryImpl implements BookmarkQueryRepository {

    private final JPAQueryFactory query;


//    @Override
//    public List<BookMark> findByUrlPathIn(List<String> totalUrls) {
//        return query.select(bookMark)
//                .from(bookMark)
//                .where(bookMark.url.in(totalUrls))
//                .fetch();
//
//    }
}
