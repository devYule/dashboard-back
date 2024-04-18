package com.yule.dashboard.bookmark;

import com.yule.dashboard.bookmark.jparepo.BookmarkJpaRepository;
import com.yule.dashboard.entities.BookMark;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookmarkRepository {
    private final BookmarkJpaRepository bookmarkJpaRepository;

    public List<BookMark> findByUserId(Long id) {
        return bookmarkJpaRepository.findByUserId(id);
    }

    public Long save(BookMark bookMark) {
        return bookmarkJpaRepository.save(bookMark).getId();
    }

    public BookMark findOneByIdAndState(Long id) {
        BookMark findBookmark = bookmarkJpaRepository.findOneByIdAndState(id, BaseState.ACTIVATED);
        if (findBookmark == null) {
            throw new ClientException(ExceptionCause.PRIMARY_KEY_IS_NOT_VALID);
        }
        return findBookmark;
    }
}
