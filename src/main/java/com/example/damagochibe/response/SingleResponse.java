package com.example.damagochibe.response;

import lombok.Data;

@Data
public class SingleResponse<T> {
    boolean success;
    int code;
    String message;
    private T data;

    public SingleResponse(T paymentResDto) {

    }
}
