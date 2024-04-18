package com.yule.dashboard.entities.superclasses;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class CreatedAt {

    @CreationTimestamp
    private LocalDateTime createdAt;

}
