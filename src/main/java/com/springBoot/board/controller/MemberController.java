package com.springBoot.board.controller;

import com.springBoot.board.controller.dto.MemberDTO;
import com.springBoot.board.controller.dto.MessageDTO;
import com.springBoot.board.controller.dto.TokenDTO;
import com.springBoot.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberService memberService;

    @GetMapping("/hello")
    public String getHello() {
        return "Hello Around Hub Studio";
    }

    @PostMapping("/join")
    public String join(@RequestBody MemberDTO memberDTO) {
        return "string";
    }

    /**
     * 회원가입
     *
     * @params memberDTO
     * @return responseEntity
     **/
    @PostMapping("/signup")
    public ResponseEntity<TokenDTO> signup (@RequestBody MemberDTO memberDTO) {
        return  memberService.signup(memberDTO);
    }

    /**
     * 중복 아이디 확인
     *
     * @params id
     * @return responseEntity
     **/
    @PostMapping("/idCheck")
    public ResponseEntity<MessageDTO> idCheck (@RequestBody MemberDTO memberDTO) {
        return memberService.idCheck(memberDTO);
    }

    /**
     * 로그인
     * 
     * @params
     * @return 
     **/
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login (@RequestBody MemberDTO memberDTO) {
        return memberService.login(memberDTO);
    }

    /**
     * 아이디 찾기
     *
     * @params
     * @return
     **/
    @PostMapping("/idSearch")
    public ResponseEntity<MessageDTO> idSearch(@RequestBody MemberDTO memberDTO) {
        return memberService.idSearch(memberDTO);
    }

    /**
     * 비밀번호 찾기
     *
     * @params
     * @return
     **/
    @PostMapping("/pwdSearch")
    public ResponseEntity<MessageDTO> pwdSearch(@RequestBody MemberDTO memberDTO) {
        return memberService.pwdSearch(memberDTO);
    }
}
