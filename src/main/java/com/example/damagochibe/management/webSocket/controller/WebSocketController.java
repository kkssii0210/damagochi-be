package com.example.damagochibe.management.webSocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
public class WebSocketController {
    @CrossOrigin(origins = "http://localhost:5000")

    @MessageMapping("/management")
    @SendTo("/topic/management")
    public String notifyManagement(String message) {
        return message;
    }
}
