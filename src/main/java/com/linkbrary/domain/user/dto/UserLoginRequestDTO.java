package com.linkbrary.domain.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Builder
public class UserLoginRequestDTO {

    @Schema(description = "사용자 이메일 주소", example = "a@naver.com")
    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    @Schema(description = "비밀번호", example = "admin1234")
    private String password;

    @Schema(description = "기기 토큰" , example = "ExponentPushToken[gUK8esHdr7943B94r2yevH]")
    private String token;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
