package com.linkbrary.domain.reminder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
@Getter
public class CreateUserReminderSettingRequestDTO {


    @Schema(description = "안 읽은지 얼마나 지났을 때 알리는지?에 대해 알람 여부 설정 input", example = "true", type = "boolean")
    @NotNull(message = "반드시 입력되어야 합니다.")
    private boolean unreadTimeAlert;

    @DateTimeFormat(pattern = "HH:mm")
    @Schema(description = "안 읽은지 얼마나 지났을 때 알리는지?에 대한 시간 input unreadTimeAlert false면 null ", example = "00:00", type = "string")
    private LocalTime unreadAlertTime;

    @NotNull(message = "반드시 입력되어야 합니다.")
    @Max(50)
    @Schema(description = "안 읽은 링크가 몇 개 쌓였을 때 알림을 보낼건지에 대한 input")
    private Long unreadFolderAlertCount;

    @Schema(description = "안 읽은지 얼마나 지났을 때 알리는지?에 대한 시간 input unreadFolderAlertCount false면 null ", example = "false", type = "boolean")
    private boolean unreadFolderAlert;
}
