package com.yule.dashboard.entities;

import com.yule.dashboard.entities.superclasses.BaseAt;
import com.yule.dashboard.entities.enums.BaseState;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
    @JoinColumn(name = "user_id")
    private Users user;

    private BaseState state;

}
