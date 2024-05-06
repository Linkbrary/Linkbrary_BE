package com.linkbrary.domain.reminder.dto;

import com.linkbrary.domain.reminder.entity.UserDirectoryReminder;
import com.linkbrary.domain.reminder.entity.UserLinkReminder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class UserReminderResponseDTO {
    private List<UserLinkReminderResponseDTO> linkReminder;
    private List<UserDirectoryReminderResponseDTO> directoryReminder;

    public static UserReminderResponseDTO from(
            List<UserLinkReminder> userLinkReminders,
            List<UserDirectoryReminder> userDirectoryReminders) {

        List<UserLinkReminderResponseDTO> linkReminderDTOs = userLinkReminders.stream()
                .map(UserLinkReminderResponseDTO::from)
                .collect(Collectors.toList());

        List<UserDirectoryReminderResponseDTO> directoryReminderDTOs = userDirectoryReminders.stream()
                .map(UserDirectoryReminderResponseDTO::from)
                .collect(Collectors.toList());

        return UserReminderResponseDTO.builder().
                linkReminder(linkReminderDTOs).directoryReminder(directoryReminderDTOs).build();
    }
}
