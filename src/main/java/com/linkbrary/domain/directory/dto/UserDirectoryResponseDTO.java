package com.linkbrary.domain.directory.dto;

import com.linkbrary.domain.directory.entity.UserDirectory;
import com.linkbrary.domain.link.dto.UserLinkDirectoryResponseDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class UserDirectoryResponseDTO {
    private Long id;
    private String directoryName;
    private Long parentFolderId;
    private List<ShortenUserDirectoryResponseDTO> childFoldersName;
    private List<UserLinkDirectoryResponseDTO> userLinks;

    public static UserDirectoryResponseDTO from(UserDirectory userDirectory) {

        List<ShortenUserDirectoryResponseDTO> childFolders = userDirectory.getChildFolders() != null
                ? userDirectory.getChildFolders().stream()
                .map(ShortenUserDirectoryResponseDTO::from)
                .collect(Collectors.toList())
                : Collections.emptyList();

        List<UserLinkDirectoryResponseDTO> userLinks = userDirectory.getUserLinks() != null
                ? userDirectory.getUserLinks().stream()
                .map(UserLinkDirectoryResponseDTO::from)
                .collect(Collectors.toList())
                : Collections.emptyList();

        return UserDirectoryResponseDTO.builder()
                .id(userDirectory.getId())
                .directoryName(userDirectory.getDirectoryName())
                .parentFolderId(userDirectory.getParentFolder() != null ? userDirectory.getParentFolder().getId() : null)
                .childFoldersName(childFolders)
                .userLinks(userLinks)
                .build();
    }

}
