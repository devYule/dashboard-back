package com.yule.dashboard.mypage.model;

import com.yule.dashboard.entities.enums.SearchbarStyle;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeleteUserVo {
    private Long id;
    private String loginId;
    private String nick;
    private String mail;
    private String pic;
    private SearchbarStyle searchbar;

}
