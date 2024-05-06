package com.linkbrary.domain.link.entity;

import com.linkbrary.common.entity.BaseTimeEntity;
import com.linkbrary.domain.directory.entity.UserDirectory;
import com.linkbrary.domain.reminder.entity.UserLinkReminder;
import com.linkbrary.domain.user.entity.Member;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserLink extends BaseTimeEntity {

    @Id
    @Column(name = "user_link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "link_id", nullable = false)
    private Link link;

    @ManyToOne
    @JoinColumn(name = "user_directory_id", referencedColumnName = "user_directory_id")
    private UserDirectory userDirectory;

    @Column(length = 255)
    private String memo;

    @Column(nullable = true)
    private String title;

    @Column
    private String summary;

    @Column
    private boolean isRead;

    @Column
    private boolean isAlarmed;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userLink")
    private UserLinkReminder userLinkReminder;

    public void updateUserDirectory(UserDirectory userDirectory) {
        this.userDirectory = userDirectory;
    }
    public void updateIsRead() {
        this.isRead = true;
    }
    public void updateIsAlarmedTrue() {
        this.isAlarmed = true;
    }
    public void updateIsAlarmedFalse() {
        this.isAlarmed = false;
    }
}
