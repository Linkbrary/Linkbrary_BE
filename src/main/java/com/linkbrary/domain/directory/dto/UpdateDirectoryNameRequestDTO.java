package com.linkbrary.domain.directory.dto;

import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Getter
public class UpdateDirectoryNameRequestDTO {

    @Schema(description = "이름 바꿀 디렉토리의 ID" , required = true)
    @NotNull(message = "디렉토리 ID는 필수 입력값입니다.")
    Long id;

    @Schema(description = "바꿀 디렉토리 이름" , required = true)
    @NotNull(message = "디렉토리 이름은 필수 입력값입니다.")
    String name;
}
