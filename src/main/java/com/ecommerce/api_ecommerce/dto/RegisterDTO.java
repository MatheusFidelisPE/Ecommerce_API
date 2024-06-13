package com.ecommerce.api_ecommerce.dto;

import com.ecommerce.api_ecommerce.model.enums.UserRole;

public record RegisterDTO(String username, String password, UserRole role, String email) {
}
