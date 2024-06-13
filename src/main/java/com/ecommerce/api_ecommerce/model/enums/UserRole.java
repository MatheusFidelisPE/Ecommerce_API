package com.ecommerce.api_ecommerce.model.enums;

public enum UserRole {
    ADM("admin"),MNG("mng"), USER("user");

    private String role;

    UserRole(String role){
        this.role = role;
    }

}
