package com.linkbrary.domain.notification.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
public class PushNotificationSettingRequestDTO {

    @DateTimeFormat(pattern = "HH:mm")
    @Schema(description = "푸시알람 시간", example = "00:00", type = "string")
    private LocalTime pushNotificationTime;

    @NotNull
    private boolean receivePushNotification;

    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private boolean sunday;
}
