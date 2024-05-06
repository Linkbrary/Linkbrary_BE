package com.linkbrary.domain.directory.controller;


import com.linkbrary.common.response.ApiResponse;
import com.linkbrary.domain.directory.dto.CreateUserDirectoryRequestDTO;
import com.linkbrary.domain.directory.dto.UpdateDirectoryNameRequestDTO;
import com.linkbrary.domain.directory.dto.UpdateUserDirectoryLocationRequestDTO;
import com.linkbrary.domain.directory.dto.UserDirectoryResponseDTO;
import com.linkbrary.domain.directory.service.UserDirectoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directories")
public class UserDirectoryController {

    private final UserDirectoryService userDirectoryService;

    @Operation(summary = "디렉토리 생성 api")
    @PostMapping
    public ApiResponse<UserDirectoryResponseDTO> createDirectory(@Valid @RequestBody CreateUserDirectoryRequestDTO createUserDirectoryRequestDTO) {
        return ApiResponse.onSuccess(userDirectoryService.createDirectory(createUserDirectoryRequestDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "해당 디렉토리 조회 api (폴더 몇개, 링크)")
    public ApiResponse<UserDirectoryResponseDTO> getDirectory(@Parameter(description = "조회할 디렉토리의 ID") @PathVariable Long id) {
        return ApiResponse.onSuccess(userDirectoryService.getDirectory(id));
    }

    @GetMapping
    @Operation(summary = "유저가 보유한 모든 디렉토리 제공 api")
    public String getUserDirectory() {
        return userDirectoryService.getAllDirectoryNames();
    }

    @Operation(summary = "디렉토리 이름 수정 api")
    @PatchMapping("/names")
    public ApiResponse<UserDirectoryResponseDTO> updateDirectoryName(@Valid @RequestBody UpdateDirectoryNameRequestDTO updateDirectoryNameRequestDTO) {
        return ApiResponse.onSuccess(userDirectoryService.updateDirectoryName(updateDirectoryNameRequestDTO));
    }

    @Operation(summary = "디렉토리 위치 수정 api")
    @PutMapping("/locations")
    public ApiResponse<UserDirectoryResponseDTO> updateDirectoryLocation(@Valid @RequestBody UpdateUserDirectoryLocationRequestDTO updateUserDirectoryLocationRequestDTO) {
        return ApiResponse.onSuccess(userDirectoryService.updateDirectoryLocation(updateUserDirectoryLocationRequestDTO));
    }

    @Operation(summary = "디렉토리 삭제 API")
    @DeleteMapping("/delete")
    public ApiResponse<String> deleteDirectory(@Parameter(description = "삭제할 디렉토리의 ID") @RequestParam Long id) {
        userDirectoryService.deleteDirectory(id);
        return ApiResponse.onSuccess("삭제 성공");
    }

}
