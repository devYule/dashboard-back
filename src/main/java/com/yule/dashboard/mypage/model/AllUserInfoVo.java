package com.yule.dashboard.mypage.model;

import com.yule.dashboard.entities.Site;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AllUserInfoVo {

    private String pic;
    private String nick;
    private String mail;
    private List<Site> sites;

}
