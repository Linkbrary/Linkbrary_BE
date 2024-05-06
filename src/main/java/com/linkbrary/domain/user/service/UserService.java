package com.linkbrary.domain.user.service;

import com.linkbrary.common.jwt.JwtTokenProvider;
import com.linkbrary.common.jwt.SecurityUtil;
import com.linkbrary.common.jwt.TokenInfo;
import com.linkbrary.common.response.code.ErrorCode;
import com.linkbrary.common.response.code.SuccessCode;
import com.linkbrary.domain.directory.entity.UserDirectory;
import com.linkbrary.domain.directory.repository.UserDirectoryRepository;
import com.linkbrary.domain.reminder.entity.UserReminderSetting;
import com.linkbrary.domain.user.dto.UserLoginRequestDTO;
import com.linkbrary.domain.user.dto.UserResponseDTO;
import com.linkbrary.domain.user.dto.UserSignUpRequestDto;
import com.linkbrary.domain.user.entity.Member;
import com.linkbrary.common.response.exception.handler.UserHandler;
import com.linkbrary.common.response.ApiResponse;
import com.linkbrary.domain.user.entity.Authority;
import com.linkbrary.domain.user.repository.MemberRepository;

import java.time.LocalTime;
import java.util.Collections;

import com.linkbrary.domain.user.repository.UserReminderSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserReminderSettingRepository userReminderSettingRepository;
    private final UserDirectoryRepository userDirectoryRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public ApiResponse signUp(UserSignUpRequestDto signUp) {
        if (memberRepository.existsByEmail(signUp.getEmail())) {
            throw new UserHandler(ErrorCode.EMAIL_ALREADY_EXIST);
        }
        Member user = Member.builder()
                .email(signUp.getEmail())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .nickname(signUp.getNickname())
                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
                .build();
        memberRepository.save(user);
        UserReminderSetting userReminderSetting = UserReminderSetting
                .builder()
                .member(user)
                .unreadFolderAlert(true)
                .unreadFolderAlertCount(10L)
                .unreadTimeAlert(true)
                .unreadAlertTime(LocalTime.NOON)
                .build();
        userReminderSettingRepository.save(userReminderSetting);
        UserDirectory userDirectory1 = UserDirectory.builder()
                .directoryName("뉴스 및 미디어")
                .member(user)
                .isAlarmed(false)
                .build();
        UserDirectory userDirectory2 = UserDirectory.builder()
                .directoryName("기술 및 개발")
                .member(user)
                .isAlarmed(false)
                .build();
        UserDirectory userDirectory3 = UserDirectory.builder()
                .directoryName("학습 자료")
                .member(user)
                .isAlarmed(false)
                .build();
        UserDirectory userDirectory4 = UserDirectory.builder()
                .directoryName("쇼핑")
                .member(user)
                .isAlarmed(false)
                .build();
        userDirectoryRepository.save(userDirectory1);
        userDirectoryRepository.save(userDirectory2);
        userDirectoryRepository.save(userDirectory3);
        userDirectoryRepository.save(userDirectory4);
        return ApiResponse.of(SuccessCode._SIGNUP_SUCCESS, "회원가입 성공!");
    }

    public ApiResponse login(UserLoginRequestDTO userLoginRequestDTO) {
        if (memberRepository.findByEmail(userLoginRequestDTO.getEmail()).orElse(null) == null) {
            throw new UserHandler(ErrorCode.MEMBER_NOT_FOUND);
        }
        try {
            // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
            // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
            UsernamePasswordAuthenticationToken authenticationToken = userLoginRequestDTO.toAuthentication();

            // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
            // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
            Authentication authentication = authenticationManagerBuilder.getObject()
                    .authenticate(authenticationToken);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
            Member member = memberRepository.findByEmail(userLoginRequestDTO.getEmail())
                    .orElseThrow();
            member.updateToken(userLoginRequestDTO.getToken());
            memberRepository.save(member);
            return ApiResponse.of(SuccessCode._LOGIN_SUCCESS, UserResponseDTO.builder().tokenInfo(tokenInfo).nickName(member.getNickname())
                    .build());
        } catch (AuthenticationException e) {
            // Handle authentication failure, e.g., incorrect password
            throw new UserHandler(ErrorCode.MEMBER_LOGIN_FAILURE);
        }
    }


//    public ResponseEntity<?> reissue(UserRequestDto.Reissue reissue) {
//        // 1. Refresh Token 검증
//        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
//            return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
//        }
//
//        // 2. Access Token 에서 User email 을 가져옵니다.
//        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());
//
//        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
//        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
//        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
//        if(ObjectUtils.isEmpty(refreshToken)) {
//            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
//        }
//        if(!refreshToken.equals(reissue.getRefreshToken())) {
//            return response.fail("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
//        }
//
//        // 4. 새로운 토큰 생성
//        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
//
//        // 5. RefreshToken Redis 업데이트
//        redisTemplate.opsForValue()
//                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
//
//        return response.success(tokenInfo, "Token 정보가 갱신되었습니다.", HttpStatus.OK);
//    }


    /*
    public ResponseEntity<?> authority() {
        String userEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("No authentication information."));
        // add ROLE_ADMIN
        member.getRoles().add(Authority.ROLE_ADMIN.name());
        memberRepository.save(member);

        return response.success();
    }
     */


    public Member getMemberFromToken() {
        String userEmail = SecurityUtil.getCurrentUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserHandler(ErrorCode.UNAUTHORIZED));
        return member;
    }

    public Member findByEmail(String email) {
        Member member =
                memberRepository
                        .findByEmail(email)
                        .orElseThrow();
        return member;
    }


}
