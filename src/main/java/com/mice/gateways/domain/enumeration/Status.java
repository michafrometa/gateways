package com.mice.gateways.domain.enumeration;

public enum Status {
    ONLINE("1"), OFFLINE("0");

    private String code;

    Status(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}