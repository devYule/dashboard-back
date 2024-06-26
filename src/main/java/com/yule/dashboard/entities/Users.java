package com.yule.dashboard.entities;

import com.yule.dashboard.entities.superclasses.CreatedAt;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.entities.enums.SearchbarStyle;
import com.yule.dashboard.search.model.SearchType;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SearchbarStyle searchbar = SearchbarStyle.LINE;

    @Builder.Default
    private BaseState state = BaseState.ACTIVATED;

//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    private List<Site> sites = new ArrayList<>();

    @Builder.Default
    private SearchType searchType = SearchType.JSOUP;


}
