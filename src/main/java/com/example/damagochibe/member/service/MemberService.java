package com.example.damagochibe.member.service;

import com.example.damagochibe.Item.mapBackground.background.entity.Mymap;
import com.example.damagochibe.Item.mapBackground.background.repository.MymapRepository;
import com.example.damagochibe.exception.NotFoundException;
import com.example.damagochibe.member.dto.request.LoginReqDto;
import com.example.damagochibe.member.dto.response.FindMemberInfoResDto;
import com.example.damagochibe.member.dto.response.LoginResDto;
import com.example.damagochibe.member.dto.response.ModifyPointResDto;
import com.example.damagochibe.member.entity.Member;
import com.example.damagochibe.member.repository.MemberRepository;
import com.example.damagochibe.member.vo.FindMongResVo;
import com.example.damagochibe.monginfo.service.MongInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MymapRepository mymapRepository;
    private final MongInfoService mongInfoService;

    @Transactional
    public Member findByMemberPlayerId(String playerId) throws RuntimeException {
        return memberRepository.findByPlayerId(playerId).orElseThrow();
    }

    @Transactional
    public FindMemberInfoResDto findMemberInfo(Long memberId) throws RuntimeException {
        String playerId = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException());

        ObjectMapper om = new ObjectMapper();

        FindMongResVo findMongResVo = null;

        try {
            findMongResVo.setMongId(mongInfoService.findMongByMember(memberId));
        } catch (Exception e) {
            log.info(e.getMessage());
            return new FindMemberInfoResDto(null, member.getPoint());
        }

        return new FindMemberInfoResDto(findMongResVo.getMongId(), member.getPoint());
    }
    @Transactional
    public ModifyPointResDto modifyPoint(Long memberId, Integer point) throws RuntimeException {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException());
        Integer prePoint = member.getPoint();
        member.setPoint(prePoint + point);
        return new ModifyPointResDto(member.getPoint());
    }
    @Transactional
    public LoginResDto login(LoginReqDto loginReqDto) {
        log.info("MemberService login - Call");
        Member member = memberRepository.findByPlayerId(loginReqDto.getPlayerId())
                .orElseThrow(NotFoundException::new);
        return LoginResDto.builder().point(member.getPoint())
                .memberId(member.getMemberId()).build();
    }
    @Transactional
    public LoginResDto register(LoginReqDto loginReqDto) {
        log.info("Member register - Call");
        Member member = Member.builder().password(loginReqDto.getPassword())
                .playerId(loginReqDto.getPlayerId()).point(0).isSocialMember(false).build();
        memberRepository.save(member);
        Long memberId = member.getMemberId();
        log.info("Member saved: {}", member.getPlayerId());


        //mymap init
        Mymap mymap = Mymap.builder()
                .mapCode("MP000")
                .memberId(memberId)
                .build();
        mymapRepository.save(mymap);
        log.info("Mymap: {}", mymap.getMapCode());

        return LoginResDto.builder().point(member.getPoint()).memberId(member.getMemberId()).mapCode(mymap.getMapCode()).build();
    }

    @Transactional
    public Member secure(String playerId) {
        Member member = memberRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new NotFoundException());
        return member;
    }

    public String getPlayerId(String playerId) {
        return memberRepository.findPlayerIdByPlayerId(playerId).orElse(null);
    }

    public Long findMemberId(String username) {
        Optional<Member> byPlayerId = memberRepository.findByPlayerId(username);
        Long memberId = byPlayerId.get().getMemberId();
        return memberId;
    }
}
