package com.linkbrary.domain.directory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
public class UpdateUserDirectoryLocationRequestDTO {

    @Schema(description = "위치 바꿀 디렉토리의 ID")
    @NotNull(message = "위치 바꿀 디렉토리 id는 필수 입력 값입니다.")
    Long directoryId;

    @Schema(description = "바꿀 위치의 부모 디렉토리 ID, 루트 디렉토리는 입력 x")
    Long requestParentDirectoryId;
}
