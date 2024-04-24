package com.yule.dashboard.entities;

import com.yule.dashboard.entities.superclasses.BaseAt;
import com.yule.dashboard.entities.enums.BaseState;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class Data extends BaseAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;

    @Enumerated(EnumType.STRING)
    private BaseState state = BaseState.ACTIVATED;

}
