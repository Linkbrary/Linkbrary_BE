package com.linkbrary.domain.link.repository;

import com.linkbrary.domain.link.entity.UserLink;
import com.linkbrary.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserLinkRepository extends JpaRepository<UserLink, Long> {
    List<UserLink> findAllByMember(Member member);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " +
            "FROM user_link ul " +
            "JOIN link l ON ul.link_id = l.link_id " +
            "WHERE ul.member_id = :memberId AND l.url = :url", nativeQuery = true)
    boolean existsByMemberAndUrl(@Param("memberId") Long memberId, @Param("url") String url);
    List<UserLink> findTop3ByMemberOrderByCreatedAtDesc(Member member);

    List<UserLink> findAllByMemberAndTitleContainingAndCreatedAtBetween(Member member, String title, LocalDateTime start, LocalDateTime end);

    List<UserLink> findAllByMemberAndMemoContainingAndCreatedAtBetween(Member member, String memo, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT ul.* FROM user_link ul " +
            "JOIN link l ON ul.link_id = l.link_id " +
            "WHERE ul.member_id = :memberId AND " +
            "similarity(l.content, :keyword) > 0.1 AND " +
            "ul.created_at BETWEEN :start AND :end " +
            "ORDER BY similarity(l.content, :keyword) DESC " +
            "LIMIT 10", nativeQuery = true)
    List<UserLink> searchByContentForMember(@Param("memberId") Long memberId, @Param("keyword") String keyword, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<UserLink> findAllByMemberAndLink_SummaryContainingAndCreatedAtBetween(Member member, String summary, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT ul.* FROM user_link ul " +
            "JOIN link l ON ul.link_id = l.link_id " +
            "WHERE ul.link_id != :id " +
            "ORDER BY l.embedding <-> (SELECT embedding FROM link WHERE link_id = :id) " +
            "LIMIT 5", nativeQuery = true)
    List<UserLink> findNearestNeighbors(@Param("id") Long id);

    @Query(value = "SELECT ul.* FROM user_link ul " +
            "JOIN link l ON ul.link_id = l.link_id " +
            "ORDER BY l.embedding <-> CAST(:embedding AS vector) " +
            "LIMIT 5", nativeQuery = true)
    List<UserLink> findNearestNeighborsByEmbedding(@Param("embedding") String embedding);


    @Query(value = "SELECT ul.* FROM user_link ul " +
            "JOIN member m ON ul.member_id = m.member_id " +
            "JOIN user_reminder_setting urs ON m.member_id = urs.member_id " +
            "WHERE ul.is_read = false " +
            "AND ul.created_at <= :thresholdDateTime " +
            "AND m.member_id = :memberId " +
            "AND ul.is_alarmed = false", nativeQuery = true)
    List<UserLink> findUnreadUserLinks(@Param("memberId") Long memberId, @Param("thresholdDateTime") LocalDateTime thresholdDateTime);

}
