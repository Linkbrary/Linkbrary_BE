package com.linkbrary.domain.link.dto;


import com.linkbrary.domain.link.entity.UserLink;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UserLinkResponseDTO {
    private final Long id;
    private final String url;
    private final String title;

    public static UserLinkResponseDTO from(UserLink userLink) {
        return UserLinkResponseDTO.builder()
                .id(userLink.getId())
                .title(userLink.getTitle())
                .url(userLink.getLink().getUrl())
                .build();
    }
}

