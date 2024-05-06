package com.linkbrary.domain.link.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Getter
public class CreateUserLinkRequestDTO {

    @Schema(description = "제목을 유저가 지정하는지 아닌지 입력받는 변수")
    @NotNull
    private boolean autoTitleSave;

    @Schema(description = "유저 지정 autoTitleSave가 false면 입력")
    private String title;

    @Schema(description = "링크를 만들 url")
    @NotNull
    private String url;

    @Size(max = 255)
    @Schema(description = "메모")
    private String memo;

    @Schema(description = "폴더를 유저가 지정하는지 아닌지 입력받는 변수")
    @NotNull
    private boolean autoFolderSave;

    @Schema(description = "직접 저장의 경우가 아닌 경우 부모 id 입력받기")
    private Long folderId;
}
