package com.linkbrary.domain.reminder.controller;

import com.linkbrary.common.response.ApiResponse;
import com.linkbrary.domain.reminder.dto.*;
import com.linkbrary.domain.reminder.service.ReminderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    @Operation(summary = "단일 링크 리마인더 생성용 api")
    @PostMapping("/links")
    public ApiResponse<UserLinkReminderResponseDTO> createLinkReminder(@Valid @RequestBody ReminderRequestDTO createLinkReminderRequestDTO) {
        return ApiResponse.onSuccess(reminderService.createLinkReminder(createLinkReminderRequestDTO));
    }
    @Operation(summary = "디렉토리 리마인더 생성용 api")
    @PostMapping("/directories")
    public ApiResponse<UserDirectoryReminderResponseDTO> createDirectoryReminder(@Valid @RequestBody ReminderRequestDTO createLinkReminderRequestDTO) {
        return ApiResponse.onSuccess(reminderService.createDirectoryReminder(createLinkReminderRequestDTO));
    }

    @Operation(summary = "유저가 설정한 리마인더 설정(10개 ,, etc) 생성하는 api")
    @PutMapping("/users")
    public ApiResponse<UserReminderSettingDTO> createUserReminderSetting(@Valid @RequestBody CreateUserReminderSettingRequestDTO createUserReminderSettingRequestDTO) {
        return ApiResponse.onSuccess(reminderService.updateUserReminderSetting(createUserReminderSettingRequestDTO));
    }

    @Operation(summary = "유저가 설정한 리마인더 설정(10개 ,, etc) 가져오는 api")
    @GetMapping("/users")
    public ApiResponse<UserReminderSettingDTO> getUserReminderSetting() {
        return ApiResponse.onSuccess(reminderService.getUserReminderSetting());
    }


    @Operation(summary = "유저가 링크에 대해 설정한 모든 리마인더 조회")
    @GetMapping("/links")
    public ApiResponse<List<UserLinkReminderResponseDTO>> getLinkReminderSettings() {
        return ApiResponse.onSuccess(reminderService.getAllLinkReminders());
    }

    @Operation(summary = "유저가 디렉토리에 대해 설정한 모든 리마인더 조회")
    @GetMapping("/directories")
    public ApiResponse<List<UserDirectoryReminderResponseDTO>> getDirectoryReminderSettings() {
        return ApiResponse.onSuccess(reminderService.getAllDirectoryReminders());
    }
    @Operation(summary = "유저가 설정한 모든 리마인더 조회")
    @GetMapping("all")
    public ApiResponse<UserReminderResponseDTO> getAllReminderSettings() {
        return ApiResponse.onSuccess(reminderService.getAllReminders());
    }

    @Operation(summary = "유저가 설정한 링크 리마인더 수정")
    @PutMapping("/links")
    public ApiResponse<UserLinkReminderResponseDTO> updateLinkReminder(@Valid @RequestBody ReminderRequestDTO updateRequestDTO) {
        return ApiResponse.onSuccess(reminderService.updateLinkReminder(updateRequestDTO));
    }

    @Operation(summary = "유저가 설정한 링크 리마인더 삭제")
    @DeleteMapping("/links/{id}")
    public ApiResponse<String> deleteLinkReminder(@PathVariable Long id) {
        reminderService.deleteLinkReminder(id);
        return ApiResponse.onSuccess("Link reminder deleted successfully");
    }

    @Operation(summary = "유저가 설정한 디렉토리 리마인더 수정")
    @PutMapping("/directories")
    public ApiResponse<UserDirectoryReminderResponseDTO> updateDirectoryReminder(@Valid @RequestBody ReminderRequestDTO updateRequestDTO) {
        return ApiResponse.onSuccess(reminderService.updateDirectoryReminder(updateRequestDTO));
    }

    @Operation(summary = "유저가 설정한 디렉토리 리마인더 삭제")
    @DeleteMapping("/directories/{id}")
    public ApiResponse<String> deleteDirectoryReminder(@PathVariable Long id) {
        reminderService.deleteDirectoryReminder(id);
        return ApiResponse.onSuccess("Directory reminder deleted successfully");
    }

    @Operation(summary = "디렉토리 리마인더 on off")
    @PutMapping("/directories/on-off")
    public ApiResponse<String> updateDirectoryReminderOnOff(@Valid @RequestBody UpdateOnOFFRequestDTO updateOnOFFRequestDTO) {
        reminderService.updateDirectoryReminderOnOFF(updateOnOFFRequestDTO);
        return ApiResponse.onSuccess("Directory reminder on/off 변경 완료");
    }
    @Operation(summary = "디렉토리 리마인더 on off")
    @PutMapping("/links/on-off")
    public ApiResponse<String> updateLinkReminderOnOff(@Valid @RequestBody UpdateOnOFFRequestDTO updateOnOFFRequestDTO) {
        reminderService.updateLinkReminderOnOFF(updateOnOFFRequestDTO);
        return ApiResponse.onSuccess("Link reminder on/off 변경 완료");
    }

}
