package com.yule.dashboard.bookmark.repositories.jparepo;

import com.yule.dashboard.bookmark.repositories.queryrepo.BookmarkQueryRepository;
import com.yule.dashboard.entities.BookMark;
import com.yule.dashboard.entities.enums.BaseState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkJpaRepository extends JpaRepository<BookMark, Long>, BookmarkQueryRepository {
    List<BookMark> findByUserId(Long id);


    BookMark findOneByIdAndState(Long id, BaseState baseState);
}
