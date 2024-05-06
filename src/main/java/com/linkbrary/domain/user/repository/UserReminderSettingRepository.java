package com.linkbrary.domain.user.repository;


import com.linkbrary.domain.reminder.entity.UserReminderSetting;
import com.linkbrary.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReminderSettingRepository extends JpaRepository<UserReminderSetting, Long> {
    Optional<UserReminderSetting> findByMember(Member member);
}
