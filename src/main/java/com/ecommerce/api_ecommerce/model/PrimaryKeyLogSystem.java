package com.ecommerce.api_ecommerce.model;

import com.ecommerce.api_ecommerce.model.enums.LogEnum;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Component
public class PrimaryKeyLogSystem implements Serializable {
    public String userId;
    public LocalDateTime editionMoment;
    public LogEnum logEnum;

}
