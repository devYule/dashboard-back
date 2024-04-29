package com.yule.dashboard.bookmark.repositories.jparepo;

import com.yule.dashboard.entities.BookmarkScreenShot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkShotJpaRepository extends JpaRepository<BookmarkScreenShot, Long> {
    BookmarkScreenShot findByBookmarkId(Long bookmarkId);
}
