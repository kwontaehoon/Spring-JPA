package com.springBoot.board.service;
import com.springBoot.board.controller.dto.MemberDTO;
import com.springBoot.board.controller.dto.MessageDTO;
import com.springBoot.board.controller.dto.TokenDTO;
import com.springBoot.board.domain.Member;
import com.springBoot.board.jwt.JwtTokenProvider;
import com.springBoot.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private final MessageDTO messageDTO;

    @Autowired
    private final TokenDTO tokenDTO;

    /**
     * 회원가입
     * 
     * @params memberDTO
     * @return responseEntity
     **/
    public ResponseEntity<TokenDTO> signup(MemberDTO memberDTO) {

        String token = jwtTokenProvider.createToken(memberDTO.getMemberId(), memberDTO.getUserId());

        Member member = Member.builder()
                .userId(memberDTO.getUserId())
                .password(bCryptPasswordEncoder.encode(memberDTO.getPassword()))
                .name(memberDTO.getName())
                .email(memberDTO.getEmail())
                .build();
        memberRepository.save(member);

        return ResponseEntity.ok().body(new TokenDTO("success", "bearer", token));
    }

    /**
     * 중복 아이디 확인
     *
     * @params memberDTO
     * @return reseponseEntity
     **/
    public ResponseEntity<MessageDTO> idCheck(MemberDTO memberDTO) {

        if(memberRepository.existsByUserId(memberDTO.getUserId())){
            messageDTO.setStatus("error");
            messageDTO.setMessage("중복된 아이디가 있습니다.");
        }else {
            messageDTO.setStatus("success");
            messageDTO.setMessage("중복된 아이디가 없습니다.");
        }
        return ResponseEntity.ok().body(messageDTO);
    }

    /**
     * 로그인
     *
     * @params memberDTO
     * @return reseponseEntity
     **/
    public ResponseEntity<TokenDTO> login(MemberDTO memberDTO) {

        Optional<Member> storedMember = memberRepository.findByUserId(memberDTO.getUserId());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String token;

        if(storedMember.isPresent() && encoder.matches(memberDTO.getPassword(), storedMember.get().getPassword())) {
            token = jwtTokenProvider.createToken(memberDTO.getMemberId(), memberDTO.getUserId());

            tokenDTO.setStatus("success");
            tokenDTO.setGrantType("Bearer");
            tokenDTO.setAccessToken(token);
        }else{
            tokenDTO.setStatus("not found");
            tokenDTO.setGrantType(null);
            tokenDTO.setAccessToken(null);
        }

        return ResponseEntity.ok().body(tokenDTO);
    }

    /**
     * 아이디 찾기
     *
     * @params memberDTO
     * @return responseEntity
     **/
    public ResponseEntity<MessageDTO> idSearch(MemberDTO memberDTO) {

        Optional<Member> storedMember = memberRepository.findByEmail(memberDTO.getEmail());

        if(storedMember.isPresent() && memberDTO.getName().equals(storedMember.get().getName())){
            messageDTO.setStatus("success");
            messageDTO.setMessage("아이디 찾기 성공");
            messageDTO.setData(storedMember);
        }else{
            messageDTO.setStatus("not found");
            messageDTO.setMessage("등록된 회원이 없습니다.");
            messageDTO.setData(null);
        }

        return ResponseEntity.ok().body(messageDTO);
    }

    /**
     * 비밀번호 찾기
     *
     * @params memberDTO
     * @return responseEntity
     **/
    public ResponseEntity<MessageDTO> pwdSearch(MemberDTO memberDTO) {

        Optional<Member> storedMember = memberRepository.findByUserId(memberDTO.getUserId());

        if(storedMember.isPresent() && memberDTO.getEmail().equals(storedMember.get().getEmail())){
            messageDTO.setStatus("success");
            messageDTO.setMessage("비밀번호 찾기 성공");
            messageDTO.setData(storedMember);
        }else{
            messageDTO.setStatus("error");
            messageDTO.setMessage("등록된 회원이 없습니다.");
            messageDTO.setData(null);
        }

        return ResponseEntity.ok().body(messageDTO);
    }
}
