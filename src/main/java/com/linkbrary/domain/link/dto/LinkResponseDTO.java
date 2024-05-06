package com.linkbrary.domain.link.dto;

import com.linkbrary.domain.link.entity.Link;
import com.linkbrary.domain.link.entity.UserLink;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LinkResponseDTO {
    private final Long id;
    private final String url;
    private final String summary;
    private final String content;

    public static LinkResponseDTO from(Link link) {
        return LinkResponseDTO.builder()
                .id(link.getId())
                .summary(link.getSummary())
                .content(link.getContent())
                .url(link.getUrl())
                .build();
    }
}
