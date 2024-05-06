package com.linkbrary.domain.link.dto;

import com.linkbrary.domain.link.entity.Link;
import com.linkbrary.domain.link.entity.UserLink;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchLinkResponseDTO {
    private final Long id;
    private String thumbnail;
    private final String url;
    private final String memo;
    private final String title;
    private final String summary;
    private final String content;


    public static SearchLinkResponseDTO from(UserLink userLink) {
        return SearchLinkResponseDTO.builder()
                .id(userLink.getId())
                .summary(userLink.getLink().getSummary())
                .content(userLink.getLink().getContent())
                .url(userLink.getLink().getUrl())
                .thumbnail(userLink.getLink().getThumbnail())
                .title(userLink.getTitle())
                .memo(userLink.getMemo())
                .build();
    }
}
