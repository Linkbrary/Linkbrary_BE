package com.linkbrary.domain.reminder.entity;

import com.linkbrary.domain.user.entity.Member;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserReminderSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_reminder_setting_id")
    private Long id;

    @Column
    private LocalTime unreadAlertTime;

    @Column(nullable = false)
    private boolean unreadTimeAlert;

    @Column
    private Long unreadFolderAlertCount;

    @Column(nullable = false)
    private boolean unreadFolderAlert;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateUserReminderSetting(LocalTime unreadAlertTime , boolean unreadTimeAlert, Long unreadFolderAlertCount, boolean unreadFolderAlert) {
        this.unreadAlertTime = unreadAlertTime;
        this.unreadTimeAlert = unreadTimeAlert;
        this.unreadFolderAlertCount = unreadFolderAlertCount;
        this.unreadFolderAlert = unreadFolderAlert;
    }
}
