package com.linkbrary.domain.reminder.entity;

import com.linkbrary.domain.directory.entity.UserDirectory;
import com.linkbrary.domain.user.entity.Member;
import lombok.*;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserDirectoryReminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userDirectoryReminderId;

    private LocalTime reminderTime;

    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_directory_reminder_days", joinColumns = @JoinColumn(name = "user_directory_reminder_days_id"))
    @Column(name = "day_of_week")
    private List<DayOfWeek> reminderDays;

    @Column
    @Builder.Default
    private boolean onoff = true;

    @OneToOne
    @JoinColumn(name = "user_directory_id", referencedColumnName = "user_directory_id")
    private UserDirectory userDirectory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void updateReminderTime(LocalTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public void updateReminderDays(List<DayOfWeek> reminderDays) {
        this.reminderDays = reminderDays;
    }

    public void updateOnoff(boolean onoff) {this.onoff = onoff;}
}
