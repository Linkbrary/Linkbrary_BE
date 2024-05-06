package com.linkbrary.domain.reminder.dto;

import com.linkbrary.domain.reminder.entity.UserReminderSetting;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class UserReminderSettingDTO {
    private Long id;
    private LocalTime unreadAlertTime;
    private boolean unreadTimeAlert;
    private Long unreadFolderAlertCount;
    private boolean unreadFolderAlert;

    public static UserReminderSettingDTO from(UserReminderSetting entity) {
        return UserReminderSettingDTO.builder()
                .id(entity.getId())
                .unreadAlertTime(entity.getUnreadAlertTime())
                .unreadTimeAlert(entity.isUnreadTimeAlert())
                .unreadFolderAlertCount(entity.getUnreadFolderAlertCount())
                .unreadFolderAlert(entity.isUnreadFolderAlert())
                .build();
    }
}
