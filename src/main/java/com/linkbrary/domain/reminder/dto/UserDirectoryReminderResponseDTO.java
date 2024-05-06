package com.linkbrary.domain.reminder.dto;

import com.linkbrary.domain.reminder.entity.UserDirectoryReminder;
import com.linkbrary.domain.reminder.entity.UserLinkReminder;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class UserDirectoryReminderResponseDTO {
    private Long id;
    private String type;
    private boolean onoff;
    private LocalTime reminderTime;
    private String title;
    private List<DayOfWeek> reminderDate;

    public static UserDirectoryReminderResponseDTO from(UserDirectoryReminder userDirectoryReminder) {
        return UserDirectoryReminderResponseDTO.builder()
                .id(userDirectoryReminder.getUserDirectoryReminderId())
                .reminderTime(userDirectoryReminder.getReminderTime())
                .onoff(userDirectoryReminder.isOnoff())
                .title(userDirectoryReminder.getUserDirectory().getDirectoryName())
                .reminderDate(userDirectoryReminder.getReminderDays())
                .type("directory")
                .build();
    }
}
