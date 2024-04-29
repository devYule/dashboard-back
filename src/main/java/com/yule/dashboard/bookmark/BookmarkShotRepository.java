package com.yule.dashboard.bookmark;

import com.yule.dashboard.bookmark.repositories.jparepo.BookmarkShotJpaRepository;
import com.yule.dashboard.entities.BookmarkScreenShot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookmarkShotRepository {
    private final BookmarkShotJpaRepository bookmarkShotJpaRepository;


    public void delete(BookmarkScreenShot bookmarkScreenShot) {
        bookmarkShotJpaRepository.delete(bookmarkScreenShot);
    }

    public BookmarkScreenShot findByBookmarkId(Long bookmarkId) {
        return bookmarkShotJpaRepository.findByBookmarkId(bookmarkId);
    }

    public BookmarkScreenShot save(BookmarkScreenShot saveBookmarkShot) {
        return bookmarkShotJpaRepository.save(saveBookmarkShot);
    }
}
