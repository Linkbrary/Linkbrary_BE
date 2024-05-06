package com.linkbrary.domain.link.dto;

import com.linkbrary.domain.link.entity.UserLink;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendedLinkDTO {
    private Long id;
    private String title;
    private String thumbnail;
    private String createdAt;

    public static RecommendedLinkDTO from(UserLink userLink){
        return RecommendedLinkDTO.builder().id(userLink.getId())
                .title(userLink.getTitle())
                .thumbnail(userLink.getLink().getThumbnail())
                .createdAt(userLink.getCreatedAt().toString())
                .build();
    }
}
