package com.linkbrary.domain.link.controller;

import com.linkbrary.common.response.ApiResponse;
import com.linkbrary.domain.link.dto.CreateUserLinkRequestDTO;
import com.linkbrary.domain.link.dto.CreateLinkRequestDTO;
import com.linkbrary.domain.link.dto.UpdateUserLinkRequestDTO;
import com.linkbrary.domain.link.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/links")
public class LinkController {

    private final LinkService linkService;

    @Operation(summary = "유저 링크 저장")
    @PostMapping("/users")
    public ApiResponse createUserLink(@Valid  @RequestBody CreateUserLinkRequestDTO createUserLinkRequestDTO) {
        return linkService.createUserLink(createUserLinkRequestDTO);
    }
    @Operation(summary = "유저 링크 삭제")
    @DeleteMapping("/users/{id}")
    private ApiResponse deleteUserLink(@PathVariable Long id) {
        return linkService.deleteUserLink(id);
    }

    @Operation(summary = "유저 링크 업데이트")
    @PutMapping("/users")
    private ApiResponse updateUserLink(@Valid @RequestBody UpdateUserLinkRequestDTO updateLinkRequestDTO) {
        return linkService.updateUserLink(updateLinkRequestDTO);
    }

    @Operation(summary = "단일 링크(크롤링+ gpt 처리) 생성용 api user 사용 x for admin")
    @PostMapping
    public ApiResponse createLink(@Valid @RequestBody CreateLinkRequestDTO createLinkRequestDTO) {
        return linkService.createLink(createLinkRequestDTO);
    }


    @Operation(summary = "단일 링크(크롤링+ gpt 처리) 조회용 api user 사용 x for admin")
    @GetMapping
    public ApiResponse getLink(@Parameter(description = "단일 링크(user링크 아님) ID") @RequestParam Long linkId) {
        return linkService.getLink(linkId);
    }

    @Operation(summary = "유저 링크 읽음 상태 업데이트 API")
    @PatchMapping("/update-read-status/{id}")
    public ApiResponse updateLinkReadStatus(@Parameter(description = "읽음 여부 업데이트할 id") @PathVariable Long id) {
        linkService.updateLinkReadStatus(id);
        return ApiResponse.onSuccess("Link read status updated successfully");
    }


    @Operation(summary = "유저 링크 검색 API")
    @GetMapping("/search")
    public ApiResponse searchLink(
            @Parameter(description = "검색 모드 (1 제목+본문 , 2 메모 , 3 요약 , 4 유사도 검색(벡터 검색)", example = "1", required = true)
            @RequestParam Integer mode,
            @Parameter(description = "날짜 범위 모드 (1 전체 기간 , 2 지난 일주일 , 3 지난 1개월 , 4 커스텀 범위)", example = "1", required = true)
            @RequestParam Integer dateRangeMode,
            @Parameter(description = "시작 날짜 (yyyy-MM-dd) 날짜 범위 모드 4일 때만 입력", example = "2023-01-01")
            @RequestParam(required = false) String startDate,
            @Parameter(description = "종료 날짜 (yyyy-MM-dd) 날짜 범위 모드 4일 때만 입력", example = "2023-12-31")
            @RequestParam(required = false) String endDate,
            @Parameter(description = "검색어", example = "안녕하세요", required = true)
            @RequestParam String search) {
        return ApiResponse.onSuccess(linkService.search(mode, dateRangeMode, startDate, endDate, search));
    }

    @Operation(summary = "linkbrary")
    @GetMapping("/linkbrary")
    public ApiResponse linkbrary(@RequestParam String url) {
        return ApiResponse.onSuccess(linkService.searchByLink(url));
    }

}
