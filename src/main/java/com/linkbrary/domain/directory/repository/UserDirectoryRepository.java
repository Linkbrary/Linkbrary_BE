package com.linkbrary.domain.directory.repository;

import com.linkbrary.domain.directory.entity.UserDirectory;
import com.linkbrary.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDirectoryRepository extends JpaRepository<UserDirectory,Long> {
    List<UserDirectory> findByMember(Member member);
    UserDirectory findByDirectoryName(String directoryName);

    @Query(value = "SELECT ud.* FROM user_directory ud " +
            "JOIN user_link ul ON ud.user_directory_id = ul.user_directory_id " +
            "JOIN user_reminder_setting urs ON ud.member_id = urs.member_id " +
            "WHERE ul.is_read = false " +
            "AND ud.member_id = :memberId " +
            "AND ud.is_alarmed = false " +
            "GROUP BY ud.user_directory_id, ud.directory_name, ud.parent_folder_id, ud.member_id, ud.is_alarmed, ud.created_at, ud.modified_at, urs.unread_folder_alert_count " +
            "HAVING COUNT(ul.user_link_id) > urs.unread_folder_alert_count", nativeQuery = true)
    List<UserDirectory> findUserDirectoriesWithUnreadLinksExceedingReminderSetting(@Param("memberId") Long memberId);

}
