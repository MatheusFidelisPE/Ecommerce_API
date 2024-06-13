package com.ecommerce.api_ecommerce.model;

import com.ecommerce.api_ecommerce.model.enums.LogEnum;
import com.ecommerce.api_ecommerce.model.enums.TypeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PrimaryKeyLogSystem.class)
public class LogSystem {
    @Id
    private String userId;
    @Id
    private LocalDateTime editionMoment;
    @Enumerated(EnumType.STRING)
    @Id
    private LogEnum logEnum;
    private String action;
    @Enumerated(EnumType.STRING)
    private TypeEntity typeEntity;
    private Long idEntity;

}
