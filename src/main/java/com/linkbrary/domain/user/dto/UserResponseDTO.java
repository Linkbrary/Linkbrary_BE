package com.linkbrary.domain.user.dto;


import com.linkbrary.common.jwt.TokenInfo;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponseDTO {

    TokenInfo tokenInfo;
    String nickName;
}
