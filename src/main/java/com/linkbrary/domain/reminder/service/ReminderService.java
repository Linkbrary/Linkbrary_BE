package com.linkbrary.domain.reminder.service;

import com.linkbrary.common.response.code.ErrorCode;
import com.linkbrary.common.response.exception.handler.ReminderHandler;
import com.linkbrary.common.response.exception.handler.UserHandler;
import com.linkbrary.common.response.exception.handler.UserLinkHandler;
import com.linkbrary.domain.directory.entity.UserDirectory;
import com.linkbrary.domain.directory.repository.UserDirectoryRepository;
import com.linkbrary.domain.link.entity.UserLink;
import com.linkbrary.domain.link.repository.UserLinkRepository;
import com.linkbrary.domain.reminder.dto.*;
import com.linkbrary.domain.reminder.entity.UserDirectoryReminder;
import com.linkbrary.domain.reminder.entity.UserLinkReminder;
import com.linkbrary.domain.reminder.entity.UserReminderSetting;
import com.linkbrary.domain.reminder.repository.UserDirectoryReminderRepository;
import com.linkbrary.domain.reminder.repository.UserLinkReminderRepository;
import com.linkbrary.domain.user.entity.Member;
import com.linkbrary.domain.user.repository.UserReminderSettingRepository;
import com.linkbrary.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReminderService {
    private final UserService userService;
    private final UserLinkRepository userLinkRepository;
    private final UserDirectoryRepository userDirectoryRepository;
    private final UserLinkReminderRepository userLinkReminderRepository;
    private final UserDirectoryReminderRepository userDirectoryReminderRepository;
    private final UserReminderSettingRepository userReminderSettingRepository;

    public UserLinkReminderResponseDTO createLinkReminder(ReminderRequestDTO createLinkReminderRequestDTO) {
        Member member = userService.getMemberFromToken();
        UserLink userLink = userLinkRepository.findById(createLinkReminderRequestDTO.getId()).orElseThrow(() -> new UserLinkHandler(ErrorCode.LINK_NOT_FOUND));
        if (userLinkReminderRepository.existsByUserLink(userLink)) {
            throw new ReminderHandler(ErrorCode.REMINDER_ALREADY_EXIST);
        }
        UserLinkReminder userLinkReminder = UserLinkReminder.builder()
                .reminderTime(createLinkReminderRequestDTO.getReminderTime())
                .member(member)
                .onoff(createLinkReminderRequestDTO.isOnoff())
                .reminderDays(createLinkReminderRequestDTO.getReminderDays())
                .userLink(userLink)
                .build();
        userLinkReminder = userLinkReminderRepository.save(userLinkReminder);
        return UserLinkReminderResponseDTO.from(userLinkReminder);
    }

    public UserDirectoryReminderResponseDTO createDirectoryReminder(ReminderRequestDTO createLinkReminderRequestDTO) {
        Member member = userService.getMemberFromToken();
        UserDirectory userDirectory = userDirectoryRepository.findById(createLinkReminderRequestDTO.getId()).orElseThrow(() -> new UserLinkHandler(ErrorCode.DIRECTORY_NOT_FOUND));
        if (userDirectoryReminderRepository.existsByUserDirectory(userDirectory)) {
            throw new ReminderHandler(ErrorCode.REMINDER_ALREADY_EXIST);
        }
        UserDirectoryReminder userDirectoryReminder = UserDirectoryReminder.builder()
                .reminderTime(createLinkReminderRequestDTO.getReminderTime())
                .member(member)
                .onoff(createLinkReminderRequestDTO.isOnoff())
                .reminderDays(createLinkReminderRequestDTO.getReminderDays())
                .userDirectory(userDirectory)
                .build();
        userDirectoryReminderRepository.save(userDirectoryReminder);
        return UserDirectoryReminderResponseDTO.from(userDirectoryReminder);
    }

    public List<UserLinkReminderResponseDTO> getAllLinkReminders() {
        Member member = userService.getMemberFromToken();
        List<UserLinkReminder> userLinkReminders = userLinkReminderRepository.findAllByMember(member);
        return userLinkReminders.stream()
                .map(UserLinkReminderResponseDTO::from)
                .collect(Collectors.toList());
    }

    public List<UserDirectoryReminderResponseDTO> getAllDirectoryReminders() {
        Member member = userService.getMemberFromToken();
        List<UserDirectoryReminder> userDirectoryReminders = userDirectoryReminderRepository.findAllByMember(member);
        return userDirectoryReminders.stream()
                .map(UserDirectoryReminderResponseDTO::from)
                .collect(Collectors.toList());
    }

    public UserReminderResponseDTO getAllReminders() {
        Member member = userService.getMemberFromToken();
        List<UserLinkReminder> userLinkReminders = userLinkReminderRepository.findAllByMember(member);
        List<UserDirectoryReminder> userDirectoryReminders = userDirectoryReminderRepository.findAllByMember(member);
        return UserReminderResponseDTO.from(userLinkReminders, userDirectoryReminders);
    }

    public UserReminderSettingDTO getUserReminderSetting() {
        Member member = userService.getMemberFromToken();
        UserReminderSetting userReminderSetting = userReminderSettingRepository.findByMember(member).orElseThrow(() -> new UserHandler(ErrorCode.USER_REMINDER_SETTING_NOT_FOUND));
        return UserReminderSettingDTO.from(userReminderSetting);
    }

    public UserReminderSettingDTO updateUserReminderSetting(CreateUserReminderSettingRequestDTO createUserReminderSettingRequestDTO){
        Member member = userService.getMemberFromToken();
        UserReminderSetting userReminderSetting = userReminderSettingRepository.findByMember(member).orElseThrow(()-> new UserHandler(ErrorCode.USER_REMINDER_SETTING_NOT_FOUND));
        userReminderSetting.updateUserReminderSetting(createUserReminderSettingRequestDTO.getUnreadAlertTime(), createUserReminderSettingRequestDTO.isUnreadTimeAlert(), createUserReminderSettingRequestDTO.getUnreadFolderAlertCount(), createUserReminderSettingRequestDTO.isUnreadFolderAlert());
        userReminderSetting = userReminderSettingRepository.save(userReminderSetting);
        return UserReminderSettingDTO.from(userReminderSetting);
    }

    public UserLinkReminderResponseDTO updateLinkReminder(ReminderRequestDTO updateRequestDTO) {
        UserLinkReminder userLinkReminder = userLinkReminderRepository.findById(updateRequestDTO.getId())
                .orElseThrow(() -> new ReminderHandler(ErrorCode.REMINDER_NOT_FOUND));
        userLinkReminder.updateReminderTime(updateRequestDTO.getReminderTime());
        userLinkReminder.updateReminderDays(updateRequestDTO.getReminderDays());
        userLinkReminder = userLinkReminderRepository.save(userLinkReminder);
        return UserLinkReminderResponseDTO.from(userLinkReminder);
    }

    public void deleteLinkReminder(Long reminderId) {
        UserLinkReminder userLinkReminder = userLinkReminderRepository.findById(reminderId)
                .orElseThrow(() -> new ReminderHandler(ErrorCode.REMINDER_NOT_FOUND));

        userLinkReminderRepository.delete(userLinkReminder);
    }

    public UserDirectoryReminderResponseDTO updateDirectoryReminder(ReminderRequestDTO updateRequestDTO) {
        UserDirectoryReminder userDirectoryReminder = userDirectoryReminderRepository.findById(updateRequestDTO.getId())
                .orElseThrow(() -> new ReminderHandler(ErrorCode.REMINDER_NOT_FOUND));
        userDirectoryReminder.updateReminderTime(updateRequestDTO.getReminderTime());
        userDirectoryReminder.updateReminderDays(updateRequestDTO.getReminderDays());
        userDirectoryReminder = userDirectoryReminderRepository.save(userDirectoryReminder);
        return UserDirectoryReminderResponseDTO.from(userDirectoryReminder);
    }

    public void deleteDirectoryReminder(Long reminderId) {
        UserDirectoryReminder userDirectoryReminder = userDirectoryReminderRepository.findById(reminderId)
                .orElseThrow(() -> new ReminderHandler(ErrorCode.REMINDER_NOT_FOUND));

        userDirectoryReminderRepository.delete(userDirectoryReminder);
    }

    public void updateDirectoryReminderOnOFF(UpdateOnOFFRequestDTO updateOnOFFRequestDTO) {
        UserDirectoryReminder userDirectoryReminder = userDirectoryReminderRepository.findById(updateOnOFFRequestDTO.getId()).orElseThrow(()-> new ReminderHandler(ErrorCode.REMINDER_NOT_FOUND));
        userDirectoryReminder.updateOnoff(updateOnOFFRequestDTO.isOnoff());
        userDirectoryReminderRepository.save(userDirectoryReminder);
    }
    public void updateLinkReminderOnOFF(UpdateOnOFFRequestDTO updateOnOFFRequestDTO) {
        UserLinkReminder userLinkReminder = userLinkReminderRepository.findById(updateOnOFFRequestDTO.getId()).orElseThrow(()-> new ReminderHandler(ErrorCode.REMINDER_NOT_FOUND));
        userLinkReminder.updateOnoff(updateOnOFFRequestDTO.isOnoff());
        userLinkReminderRepository.save(userLinkReminder);
    }

}
