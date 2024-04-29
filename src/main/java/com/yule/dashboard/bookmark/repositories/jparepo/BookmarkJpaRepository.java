package com.yule.dashboard.bookmark.repositories.jparepo;

import com.yule.dashboard.bookmark.repositories.queryrepo.BookmarkQueryRepository;
import com.yule.dashboard.entities.Bookmark;
import com.yule.dashboard.entities.enums.BaseState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkJpaRepository extends JpaRepository<Bookmark, Long>, BookmarkQueryRepository {
    List<Bookmark> findByUserIdAndState(Long id, BaseState state, Sort sort);

    Page<Bookmark> findByUserIdAndState(Long id, BaseState state, Pageable page);

    List<Bookmark> findByUserIdAndState(Long id, BaseState state);

    Bookmark findByIdAndStateAndUserId(Long id, BaseState state, Long userId);

    List<Bookmark> findByUrlIn(List<String> urls);
}
