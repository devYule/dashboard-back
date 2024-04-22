package com.yule.dashboard.mypage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class MailCheckInfo {
    private String mail;
    private String code;
}
