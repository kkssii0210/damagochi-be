package com.example.damagochibe.auth.oauth.oauthconfig;

import com.example.damagochibe.auth.oauth.dto.SocialOauthToken;
import com.example.damagochibe.auth.oauth.dto.SocialUserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface SocialOauth {
    String getOauthRedirectURL();
    ResponseEntity<String> requestAccessToken(String code);
    SocialOauthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException;
    ResponseEntity<String> requestUserInfo(SocialOauthToken oauthToken);
    SocialUserDto getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException;
}
