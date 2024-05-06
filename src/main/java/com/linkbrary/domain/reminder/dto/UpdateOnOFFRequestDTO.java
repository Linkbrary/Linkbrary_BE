package com.linkbrary.domain.reminder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateOnOFFRequestDTO {
    @NotNull(message = "onoff값은 필수 입력 값입니다.")
    @Schema(description = "onoff 설정", example = "true")
    private boolean onoff;

    @NotNull(message = "반드시 입력되어야 합니다.")
    @Schema(description = "리마인더(디렉토리, 링크) id", example = "true", type = "boolean")
    private Long id;
}
