package com.linkbrary.domain.reminder.entity;

import com.linkbrary.domain.link.entity.UserLink;
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
public class UserLinkReminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userLinkReminderId;

    private LocalTime reminderTime;

    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_link_reminder_days", joinColumns = @JoinColumn(name = "user_link_reminder_days_id"))
    @Column(name = "day_of_week")
    private List<DayOfWeek> reminderDays;

    private boolean onoff;

    @OneToOne
    @JoinColumn(name = "user_link_id", referencedColumnName = "user_link_id")
    private UserLink userLink;

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
