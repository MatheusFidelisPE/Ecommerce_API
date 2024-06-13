package com.ecommerce.api_ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@Data
public class ProductDto {


    private int productId;
    @NotNull
    private String name;
    @NotNull
    private String description;
    private Double price;


}
