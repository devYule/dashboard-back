package com.yule.dashboard.entities;

import com.yule.dashboard.entities.superclasses.CreatedAt;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.entities.enums.SearchbarStyle;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Users extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private String pw;
    private String nick;
    private String mail;
    private String pic;
    private SearchbarStyle searchbar;
    private BaseState state;

}
