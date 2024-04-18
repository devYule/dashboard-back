package com.yule.dashboard.user.jparepo;

import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.BaseState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserJpaRepository extends JpaRepository<Users, Long> {
    Users findByLoginIdAndState(String loginId, BaseState state);

    Optional<Users> findByIdAndState(Long id, BaseState state);
}
