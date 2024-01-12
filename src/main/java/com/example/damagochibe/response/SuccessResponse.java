package com.example.damagochibe.response;

import com.example.damagochibe.code.BasicCode;
import lombok.Data;

@Data
public class SuccessResponse {
    private String code;
    private String message;

    public SuccessResponse(BasicCode managementStateCode){
        this.code = managementStateCode.getCode();
        this.message = managementStateCode.getMessage();
    }
}
