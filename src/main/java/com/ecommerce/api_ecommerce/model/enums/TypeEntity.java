package com.ecommerce.api_ecommerce.model.enums;

public enum TypeEntity {
    PRODUCT("prd");

    private String type;
    TypeEntity(String type){
        this.type = type;
    }
    public String getType(){
        return type;
    }
}
