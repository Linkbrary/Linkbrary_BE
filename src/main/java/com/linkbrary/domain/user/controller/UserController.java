package com.linkbrary.domain.user.controller;

import com.linkbrary.domain.user.dto.HomeResponseDTO;
import com.linkbrary.domain.user.dto.UserLoginRequestDTO;
import com.linkbrary.domain.user.dto.UserSignUpRequestDto;
import com.linkbrary.domain.user.service.HomeService;
import com.linkbrary.domain.user.service.UserService;
import com.linkbrary.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService usersService;
    private final HomeService homeService;

    @CrossOrigin
    @Operation(summary = "회원가입 API")
    @PostMapping("/sign-up")
    public ApiResponse signUp(@RequestBody  @Valid UserSignUpRequestDto userSignUpRequestDto) {
        return usersService.signUp(userSignUpRequestDto);
    }
    @CrossOrigin
    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public ApiResponse login(@RequestBody @Valid UserLoginRequestDTO userLoginRequestDTO) {
        return usersService.login(userLoginRequestDTO);
    }

    @Operation(summary = "홈 화면 API")
    @GetMapping("/home")
    public ApiResponse<HomeResponseDTO> home() {
        return homeService.getHomeData();
    }
}
