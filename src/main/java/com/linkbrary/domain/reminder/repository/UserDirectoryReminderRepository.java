package com.linkbrary.domain.reminder.repository;

import com.linkbrary.domain.directory.entity.UserDirectory;
import com.linkbrary.domain.reminder.entity.UserDirectoryReminder;
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
public interface UserDirectoryReminderRepository extends JpaRepository<UserDirectoryReminder,Long> {
    List<UserDirectoryReminder> findAllByMember(Member member);
    boolean existsByUserDirectory(UserDirectory userDirectory);


    @Query(value = "SELECT * FROM user_directory_reminder r " +
            "JOIN user_directory_reminder_days d ON r.user_directory_reminder_id = d.user_directory_reminder_days_id " +
            "WHERE d.day_of_week = :currentDay " +
            "AND r.onoff = true " +
            "AND r.reminder_time BETWEEN :start AND :end", nativeQuery = true)
    List<UserDirectoryReminder> findAllByReminderDaysAndReminderTimeBetween(
            @Param("currentDay") String currentDay,
            @Param("start") LocalTime start,
            @Param("end") LocalTime end);
}
