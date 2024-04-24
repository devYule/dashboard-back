package com.yule.dashboard.user.repositories.jparepo;

import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.BaseState;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserJpaRepository extends JpaRepository<Users, Long> {
    Users findByLoginId(String loginId);



    boolean existsByNick(String nick);

    boolean existsByMail(String mail);

}
