package com.linkbrary.domain.notification;


import com.linkbrary.domain.directory.entity.UserDirectory;
import com.linkbrary.domain.directory.repository.UserDirectoryRepository;
import com.linkbrary.domain.link.entity.UserLink;
import com.linkbrary.domain.link.repository.UserLinkRepository;
import com.linkbrary.domain.reminder.entity.UserDirectoryReminder;
import com.linkbrary.domain.reminder.entity.UserLinkReminder;
import com.linkbrary.domain.reminder.repository.UserDirectoryReminderRepository;
import com.linkbrary.domain.reminder.repository.UserLinkReminderRepository;
import com.linkbrary.domain.user.entity.Member;
import com.linkbrary.domain.user.repository.MemberRepository;
import com.linkbrary.domain.user.repository.UserReminderSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.linkbrary.common.util.CallExternalApi.sendNotification;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendNotificationService {
    private final UserReminderSettingRepository userReminderSettingRepository;
    private final UserLinkRepository userLinkRepository;
    private final UserDirectoryRepository userDirectoryRepository;
    private final UserLinkReminderRepository userLinkReminderRepository;
    private final UserDirectoryReminderRepository userDirectoryReminderRepository;
    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 12 * * ?", zone = "Asia/Seoul")
    public void resetAlarmedStatus() {
        List<UserLink> userLinks = userLinkRepository.findAll();
        List<UserDirectory> userDirectories = userDirectoryRepository.findAll();
        userLinks.forEach(UserLink::updateIsAlarmedFalse);
        userDirectories.forEach(UserDirectory::updateIsAlarmedFalse);
    }

    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    public void sendUnreadUserDirectoryNotifications() {
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            List<UserDirectory> userDirectories = userDirectoryRepository.findUserDirectoriesWithUnreadLinksExceedingReminderSetting(member.getId());
            if (!userDirectories.isEmpty()) {
                String title = member.getNickname() + "님의 디렉토리에 안읽은 링크가 쌓여있어요!";
                String body = "“" + userDirectories.get(0).getDirectoryName() + "” 디렉토리를 확인하세요!";
                Long id = userDirectories.get(0).getId();
                if (member.getToken() != null) {
                    sendNotification(member.getToken(), title, body, id.toString());
                }
            }
            userDirectories.forEach(UserDirectory::updateIsAlarmedTrue);
            userDirectoryRepository.saveAll(userDirectories);

        }
    }

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void sendUnreadUserLinkNotifications() {
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            LocalTime unreadAlertTime = member.getUserReminderSetting().getUnreadAlertTime();
            Duration duration = Duration.ofHours(unreadAlertTime.getHour())
                    .plusMinutes(unreadAlertTime.getMinute())
                    .plusSeconds(unreadAlertTime.getSecond());
            LocalDateTime thresholdDateTime = LocalDateTime.now().minus(duration);
            List<UserLink> userLinks = userLinkRepository.findUnreadUserLinks(member.getId(), thresholdDateTime);
            if (!userLinks.isEmpty()) {
                if (member.getToken() != null) {
                    String title = member.getNickname() + "님이 저장 후 안읽은 링크가 있어요!";
                    String body = "“" + userLinks.get(0).getLink().getTitle() + "” 링크를 확인하세요!";
                    Long id = userLinks.get(0).getUserDirectory().getId();
                    sendNotification(member.getToken(), title, body, id.toString());
                }
            }
            userLinks.forEach(UserLink::updateIsAlarmedTrue);
            userLinkRepository.saveAll(userLinks);
        }
    }


    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void sendDirectoryReminderByMinute() {
        LocalDateTime currentTime = LocalDateTime.now();
        DayOfWeek currentDay = currentTime.getDayOfWeek();
        LocalTime currentHourMinute = currentTime.toLocalTime().withSecond(0).withNano(0);
        // 현재 시간 ±30초 범위 설정
        LocalTime startRange = currentHourMinute.minusSeconds(30);
        LocalTime endRange = currentHourMinute.plusSeconds(30);
        List<UserDirectoryReminder> userDirectoryReminders = userDirectoryReminderRepository.findAllByReminderDaysAndReminderTimeBetween(currentDay.toString(), startRange, endRange);
        for (UserDirectoryReminder reminder : userDirectoryReminders) {
            Member user = reminder.getMember();
            String token = user.getToken();
            if (token != null) {
                String title = user.getNickname() + "님이 설정하신 시간입니다!";
                String body = "“" + reminder.getUserDirectory().getDirectoryName() + "” 디렉토리를 확인하세요!";
                Long id = reminder.getUserDirectory().getId();
                sendNotification(token, title, body, id.toString());
            }
        }
    }

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void sendLinkReminderByMinute() {
        LocalDateTime currentTime = LocalDateTime.now();
        DayOfWeek currentDay = currentTime.getDayOfWeek();
        LocalTime currentHourMinute = currentTime.toLocalTime().withSecond(0).withNano(0);
        // 현재 시간 ±30초 범위 설정
        LocalTime startRange = currentHourMinute.minusSeconds(30);
        LocalTime endRange = currentHourMinute.plusSeconds(30);
        List<UserLinkReminder> userLinkReminders = userLinkReminderRepository.findAllByReminderDaysAndReminderTimeBetween(currentDay.toString(), startRange, endRange);
        for (UserLinkReminder reminder : userLinkReminders) {
            Member user = reminder.getMember();
            System.out.println(user.getToken());
            Long id = reminder.getUserLink().getUserDirectory().getId();
            String title = user.getNickname() + "님이 설정하신 시간입니다!";
            String body = "“" + reminder.getUserLink().getTitle() + "” 링크를 확인하세요!";
            sendNotification(user.getToken(), title, body, id.toString());
        }
    }
}

