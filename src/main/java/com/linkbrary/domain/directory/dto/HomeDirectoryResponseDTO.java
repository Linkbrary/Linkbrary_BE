package com.linkbrary.domain.directory.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomeDirectoryResponseDTO {
    private Long id;
    private String name;
    private int linkCount;
}
