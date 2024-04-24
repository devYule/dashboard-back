package com.yule.dashboard.mypage.model.req;

import java.util.List;

public record GetAllUserInfoData(
        String pic,
        String nick,
        String mail,
        List<Integer> sites
) {
}
