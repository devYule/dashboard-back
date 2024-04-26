package com.yule.dashboard.bookmark;

import com.yule.dashboard.bookmark.model.data.req.BookmarkAddData;
import com.yule.dashboard.bookmark.model.data.resp.BookmarkData;
import com.yule.dashboard.bookmark.model.data.resp.BookmarkDataPage;
import com.yule.dashboard.entities.BookMark;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.Widget;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.pbl.BaseResponse;
import com.yule.dashboard.pbl.security.SecurityFacade;
import com.yule.dashboard.user.UserRepository;
import com.yule.dashboard.widget.WidgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final WidgetRepository widgetRepository;
    private final SecurityFacade facade;

    public List<BookmarkData> getBookmarks() {
        Long id = facade.getId();
        return bookmarkRepository.findByUserIdDesc(id).stream().map(b -> new BookmarkData(
                b.getId(),
                b.getTitle(),
                b.getUrl(),
                b.getMemo()
        )).toList();
    }

    public BookmarkDataPage getBookmarksPage(int page) {
        int maxContentSize = 5;
        int offset = page - 1;
        PageRequest pageable = PageRequest.of(offset, maxContentSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<BookMark> findBookmarks = bookmarkRepository.findByUserIdAndState(facade.getId(), BaseState.ACTIVATED, pageable);


        return BookmarkDataPage.builder()
                .hasNext(findBookmarks.hasNext() ? 1 : 0)
                .bookmarks(findBookmarks.hasNext() ? findBookmarks.toList().subList(0, 5).stream()
                        .map(b -> new BookmarkData(b.getId(), b.getTitle(), b.getUrl(), b.getMemo())).toList()
                        :
                        findBookmarks.stream()
                                .map(b -> new BookmarkData(b.getId(), b.getTitle(), b.getUrl(), b.getMemo())).toList())
                .build();
    }

    @Transactional
    public BaseResponse addBookmark(BookmarkAddData data) {
        Users findUser = userRepository.findById(facade.getId());
        BookMark bookMark = new BookMark(data.title(), data.url(), data.memo());
        bookMark.setUser(findUser);
        return BaseResponse.builder().value(bookmarkRepository.save(bookMark).getId()).build();
    }

    @Transactional
    public BaseResponse removeBookmark(Long id) {
        BookMark findBookmark = bookmarkRepository.findByIdAndStateAndUserId(id, facade.getId());
        List<Widget> findWidgets = widgetRepository.findByBookMarkAndState(findBookmark, BaseState.ACTIVATED);
        findWidgets.forEach(w -> w.setState(BaseState.DEACTIVATED));
        findBookmark.setState(BaseState.DEACTIVATED);

        return BaseResponse.builder().value(findBookmark.getId()).build();
    }
}
