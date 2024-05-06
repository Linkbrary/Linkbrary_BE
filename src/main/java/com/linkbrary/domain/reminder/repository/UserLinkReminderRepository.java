package com.linkbrary.domain.reminder.repository;


import com.linkbrary.domain.link.entity.UserLink;
import com.linkbrary.domain.reminder.entity.UserLinkReminder;
import com.linkbrary.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface UserLinkReminderRepository extends JpaRepository<UserLinkReminder, Long> {
    List<UserLinkReminder> findAllByMember(Member member);
    boolean existsByUserLink(UserLink userLink);

    @Query(value = "SELECT * FROM user_link_reminder r " +
            "JOIN user_link_reminder_days d ON r.user_link_reminder_id = d.user_link_reminder_days_id " +
            "WHERE d.day_of_week = :currentDay " +
            "AND r.onoff = true " +
            "AND r.reminder_time BETWEEN :start AND :end", nativeQuery = true)
    List<UserLinkReminder> findAllByReminderDaysAndReminderTimeBetween(
            @Param("currentDay") String currentDay,
            @Param("start") LocalTime start,
            @Param("end") LocalTime end);
}
