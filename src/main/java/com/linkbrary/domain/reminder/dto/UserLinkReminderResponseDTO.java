package com.linkbrary.domain.reminder.dto;

import com.linkbrary.domain.reminder.entity.UserLinkReminder;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class UserLinkReminderResponseDTO {
    private Long id;
    private String type;
    private boolean onoff;
    private String title;
    private LocalTime reminderTime;
    private List<DayOfWeek> reminderDate;

    public static UserLinkReminderResponseDTO from(UserLinkReminder userLinkReminder) {
        return UserLinkReminderResponseDTO.builder()
                .id(userLinkReminder.getUserLinkReminderId())
                .type("link")
                .onoff(userLinkReminder.isOnoff())
                .title(userLinkReminder.getUserLink().getTitle())
                .reminderTime(userLinkReminder.getReminderTime())
                .reminderDate(userLinkReminder.getReminderDays())
                .build();
    }
}
