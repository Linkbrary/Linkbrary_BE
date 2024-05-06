package com.linkbrary.domain.link.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DirectoryInfo {
    private String name;
    private Long id;
}
