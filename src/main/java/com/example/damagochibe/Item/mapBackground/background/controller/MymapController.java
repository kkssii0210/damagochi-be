package com.example.damagochibe.Item.mapBackground.background.controller;

import com.example.damagochibe.Item.mapBackground.background.service.MymapService;
import com.example.damagochibe.Item.mapBackground.background.dto.FindMymapResDto;
import com.example.damagochibe.code.MymapStateCode;
import com.example.damagochibe.exception.NotFoundMymapException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.damagochibe.response.ErrorResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member/background")
public class MymapController {
    private final MymapService mymapService;

    @GetMapping("/mymap")
    public ResponseEntity<Object> findMymap(@RequestHeader(value = "MemberId") String memberIdStr){
        log.info("findMymap - Call");
        try {
            FindMymapResDto ret = mymapService.findMymap(memberIdStr);
            return ResponseEntity.status(HttpStatus.OK).body(ret);
        }catch (NotFoundMymapException e){
            log.info("code : {}, message : {}", MymapStateCode.MYMAP_ERROR.getCode(), MymapStateCode.MYMAP_ERROR.name());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(MymapStateCode.MYMAP_ERROR));
        }
        catch (Exception e){
            System.out.println(e);
            log.info("code : {}, message : {}", MymapStateCode.UNKNOWN.getCode(), MymapStateCode.UNKNOWN.name());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(MymapStateCode.UNKNOWN));
        }
    }
}
