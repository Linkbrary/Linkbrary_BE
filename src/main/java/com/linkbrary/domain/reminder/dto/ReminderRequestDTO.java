package com.linkbrary.domain.reminder.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@Data
public class ReminderRequestDTO {

    @NotNull(message = "반드시 입력되어야 합니다.")
    @Schema(description = "리마인더(디렉토리, 링크) id", example = "true", type = "boolean")
    private Long id;

    @NotNull(message = "반드시 입력되어야 합니다.")
    @Schema(description = "알람 onoff", example = "true", type = "boolean")
    private boolean onoff;

    @NotNull(message = "반드시 입력되어야 합니다.")
    @DateTimeFormat(pattern = "HH:mm")
    @Schema(description = "푸시알람 시간", example = "00:00", type = "string")
    private LocalTime reminderTime;

    @NotNull(message = "반드시 입력되어야 합니다.")
    @Schema(description = "푸시알람 요일")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<DayOfWeek> reminderDays;
}
