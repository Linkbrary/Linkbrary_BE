package com.linkbrary.domain.directory.dto;

import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Getter
public class CreateUserDirectoryRequestDTO {

    @Schema(description = "부모 폴더 ID, 안 넣으면 null",example = "1")
    Long parentFolderId;

    @NotNull(message = "디렉토리 이름은 필수 입력값입니다.")
    @Schema(description = "디렉토리 내용")
    String directoryName;
}
