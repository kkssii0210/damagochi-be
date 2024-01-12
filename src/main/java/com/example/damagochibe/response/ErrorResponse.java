package com.example.damagochibe.response;

import com.example.damagochibe.code.BasicCode;
import lombok.Data;

@Data
public class ErrorResponse {
    private String code;
    private String message;

    public ErrorResponse(BasicCode managementStateCode){
        this.code = managementStateCode.getCode();
        this.message = managementStateCode.getMessage();
    }
}
