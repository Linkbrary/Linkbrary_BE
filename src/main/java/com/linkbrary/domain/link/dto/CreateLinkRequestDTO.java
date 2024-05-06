package com.linkbrary.domain.link.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateLinkRequestDTO {
    @Schema(description = "그냥 단순 링크 생성용 url(데이터 만들기 용)")
    @NotNull
    private String url;
}
