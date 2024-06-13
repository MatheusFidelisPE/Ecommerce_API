package com.ecommerce.api_ecommerce.model.enums;

public enum LogEnum {
    CREATE("create"), UPDATE("update"), DELETE("delete"), GET("get");
    private String log;
    LogEnum(String log){
        this.log = log;
    }

    public String getLog(){
        return log;
    }
}
