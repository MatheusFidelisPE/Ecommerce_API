package com.ecommerce.api_ecommerce.model.enums;

public enum UserRole {
    ADM("admin"),MNG("mng"), USER("user");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
    public static UserRole fromRole(String role) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.getRole().equalsIgnoreCase(role)) {
                return userRole;
            }
        }
        return null;
    }
}
