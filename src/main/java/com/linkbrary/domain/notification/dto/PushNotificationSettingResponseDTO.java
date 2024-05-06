package com.linkbrary.domain.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Builder
@Getter
public class PushNotificationSettingResponseDTO {
    @Schema(description = "푸시알람 시간", example = "00:00", type = "string")
    private LocalTime pushNotificationTime;
    private boolean receivePushNotification;
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private boolean sunday;
}
