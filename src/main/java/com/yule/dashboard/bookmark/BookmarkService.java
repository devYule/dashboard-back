package com.yule.dashboard.bookmark;

import com.yule.dashboard.bookmark.model.data.req.BookmarkAddData;
import com.yule.dashboard.bookmark.model.data.resp.BookmarkData;
import com.yule.dashboard.entities.BookMark;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.embeddable.UrlPath;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.pbl.BaseResponse;
import com.yule.dashboard.pbl.PrimaryKeyData;
import com.yule.dashboard.pbl.security.SecurityFacade;
import com.yule.dashboard.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final SecurityFacade facade;

    public List<BookmarkData> getBookmarks() {
        Long id = facade.getId();
        return bookmarkRepository.findByUserId(id).stream().map(b -> new BookmarkData(
                b.getId(),
                b.getTitle(),
                b.getUrl().getUrl(),
                b.getMemo()
        )).toList();
    }

    public BaseResponse addBookmark(BookmarkAddData data) {
        Users findUser = userRepository.findByIdAndState(facade.getId(), BaseState.ACTIVATED);
        BookMark bookMark = new BookMark(data.title(), new UrlPath(data.url()), data.memo());
        bookMark.setUser(findUser);
        return BaseResponse.builder().value(bookmarkRepository.save(bookMark)).build();
    }

    @Transactional
    public BaseResponse removeBookmark(Long id) {
        BookMark findBookmark = bookmarkRepository.findOneByIdAndState(id);
        findBookmark.setState(BaseState.DEACTIVATED);

        return BaseResponse.builder().value(findBookmark.getId()).build();
    }
}
