package com.linkbrary.domain.directory.dto;

import com.linkbrary.domain.directory.entity.UserDirectory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShortenUserDirectoryResponseDTO {
    private String directoryName;
    private Long directoryId;
    private String thumbNail;

    public static ShortenUserDirectoryResponseDTO from(UserDirectory userDirectory) {
        return ShortenUserDirectoryResponseDTO
                .builder()
                .directoryName(userDirectory.getDirectoryName())
                .thumbNail(userDirectory.getUserLinks() != null && !userDirectory.getUserLinks().isEmpty()
                        ? userDirectory.getUserLinks().get(0).getLink().getThumbnail()
                        : null)
                .directoryId(userDirectory.getId())
                .build();
    }

}
