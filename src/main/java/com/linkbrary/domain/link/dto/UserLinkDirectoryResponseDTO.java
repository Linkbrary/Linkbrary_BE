package com.linkbrary.domain.link.dto;

import com.linkbrary.domain.link.entity.UserLink;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class UserLinkDirectoryResponseDTO {
    private Long id;
    private String title;
    private String thumbnail;
    private String summary;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static UserLinkDirectoryResponseDTO from(UserLink userLink) {
        return UserLinkDirectoryResponseDTO.builder()
                .id(userLink.getId())
                .title(userLink.getLink().getTitle())
                .createdAt(userLink.getCreatedAt())
                .summary(userLink.getLink().getSummary())
                .modifiedAt(userLink.getModifiedAt())
                .thumbnail(userLink.getLink().getThumbnail())
                .build();
    }
}
