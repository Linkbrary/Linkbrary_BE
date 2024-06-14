package com.linkbrary.domain.user.service;


import com.linkbrary.common.response.ApiResponse;
import com.linkbrary.domain.directory.dto.HomeDirectoryResponseDTO;
import com.linkbrary.domain.directory.repository.UserDirectoryRepository;
import com.linkbrary.domain.link.dto.RecommendedLinkDTO;
import com.linkbrary.domain.link.entity.UserLink;
import com.linkbrary.domain.link.repository.UserLinkRepository;
import com.linkbrary.domain.user.dto.HomeResponseDTO;
import com.linkbrary.domain.user.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final UserService userService;


    private final UserDirectoryRepository userDirectoryRepository;


    private final UserLinkRepository userLinkRepository;

    public ApiResponse<HomeResponseDTO> getHomeData() {
        Member member = userService.getMemberFromToken();
        List<UserLink> recommendUserLinks = userLinkRepository.findTop3ByMemberOrderByCreatedAtDesc(member);
        List<UserLink> userLinks = userLinkRepository.findAllByMember(member);
        int totalLinks = userLinks.size();
        int readLinks = (int) userLinks.stream().filter(UserLink::isRead).count();

        List<HomeDirectoryResponseDTO> directories = userDirectoryRepository.findByMember(member).stream()
                .map(directory -> HomeDirectoryResponseDTO.builder()
                        .id(directory.getId())
                        .name(directory.getDirectoryName())
                        .linkCount(directory.getUserLinks().size())
                        .build())
                .collect(Collectors.toList());

        List<RecommendedLinkDTO> recommendedLinkDTOS = recommendUserLinks.stream()
                .map(RecommendedLinkDTO::from)
                .toList();

        return ApiResponse.onSuccess(HomeResponseDTO.builder()
                .totalLinks(totalLinks)
                .readLinks(readLinks)
                .directories(directories)
                .recommendedLink(recommendedLinkDTOS)
                .build());
    }

    private List<UserLink> getRecommendedLink(List<UserLink> userLinks) {
        // 추천 링크 로직 (예: 가장 최근에 추가된 링크)
        return userLinks.isEmpty() ? Collections.emptyList() : List.of(userLinks.get(0));
    }

}
