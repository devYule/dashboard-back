package com.yule.dashboard.entities.superclasses;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseAt extends CreatedAt{

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
