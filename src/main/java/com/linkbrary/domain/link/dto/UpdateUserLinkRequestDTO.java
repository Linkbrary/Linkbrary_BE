package com.linkbrary.domain.link.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateUserLinkRequestDTO {

    @Schema(description = "유저 링크 id")
    @NotNull(message = "id는 필수 입력 값입니다.")
    private Long id;

    @Schema(description = "유저 지정 autoTitleSave가 false면 입력")
    private String title;

    @Size(max = 255)
    @Schema(description = "메모")
    private String memo;


    @Size(max = 255)
    @Schema(description = "요약")
    private String summary;

}
