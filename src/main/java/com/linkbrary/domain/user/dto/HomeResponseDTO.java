package com.linkbrary.domain.user.dto;

import com.linkbrary.domain.directory.dto.HomeDirectoryResponseDTO;
import com.linkbrary.domain.link.dto.RecommendedLinkDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HomeResponseDTO {
    private int totalLinks;
    private int readLinks;
    private List<HomeDirectoryResponseDTO> directories;
    private List<RecommendedLinkDTO> recommendedLink;
}
