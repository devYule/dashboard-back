package com.yule.dashboard.bookmark;

import com.yule.dashboard.bookmark.repositories.jparepo.BookmarkJpaRepository;
import com.yule.dashboard.entities.Bookmark;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookmarkRepository {
    private final BookmarkJpaRepository bookmarkJpaRepository;

    public List<Bookmark> findByUserIdDesc(Long id) {
        return bookmarkJpaRepository.findByUserIdAndState(id, BaseState.ACTIVATED, Sort.by(Sort.Direction.DESC, "id"));
    }

    public Bookmark save(Bookmark bookMark) {
        return bookmarkJpaRepository.save(bookMark);
    }

    public Bookmark findByIdAndStateAndUserId(Long id, Long userId) {
        Bookmark findBookmark = bookmarkJpaRepository.findByIdAndStateAndUserId(id, BaseState.ACTIVATED, userId);
        if (findBookmark == null) {
            throw new ClientException(ExceptionCause.PRIMARY_KEY_IS_NOT_VALID);
        }
        return findBookmark;
    }

    public List<Bookmark> findByUrlIn(List<String> totalUrls) {
        return bookmarkJpaRepository.findByUrlIn(totalUrls);
    }

    public Page<Bookmark> findByUserIdAndState(Long userId, BaseState state, PageRequest page) {
        return bookmarkJpaRepository.findByUserIdAndState(userId, state, page);
    }

    public void flush() {
        bookmarkJpaRepository.flush();
    }


}
