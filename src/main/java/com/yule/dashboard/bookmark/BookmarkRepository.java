package com.yule.dashboard.bookmark;

import com.yule.dashboard.bookmark.repositories.jparepo.BookmarkJpaRepository;
import com.yule.dashboard.entities.BookMark;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookmarkRepository {
    private final BookmarkJpaRepository bookmarkJpaRepository;

    public List<BookMark> findByUserIdDesc(Long id) {
        return bookmarkJpaRepository.findByUserIdAndState(id, BaseState.ACTIVATED, Sort.by(Sort.Direction.DESC, "id"));
    }

    public BookMark save(BookMark bookMark) {
        return bookmarkJpaRepository.save(bookMark);
    }

    public BookMark findByIdAndStateAndUserId(Long id, Long userId) {
        BookMark findBookmark = bookmarkJpaRepository.findByIdAndStateAndUserId(id, BaseState.ACTIVATED, userId);
        if (findBookmark == null) {
            throw new ClientException(ExceptionCause.PRIMARY_KEY_IS_NOT_VALID);
        }
        return findBookmark;
    }

    public List<BookMark> findByUrlIn(List<String> totalUrls) {
        return bookmarkJpaRepository.findByUrlIn(totalUrls);
    }
}
