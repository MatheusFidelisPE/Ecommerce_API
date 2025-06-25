package com.ecommerce.api_ecommerce.dto;

import com.ecommerce.api_ecommerce.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Builder
public record RegisterDTO(String username, String password, UserRole role, String email) {
}
