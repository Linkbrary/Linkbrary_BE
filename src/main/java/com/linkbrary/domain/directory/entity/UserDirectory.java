package com.linkbrary.domain.directory.entity;

import com.linkbrary.common.entity.BaseTimeEntity;
import com.linkbrary.domain.link.entity.UserLink;
import com.linkbrary.domain.reminder.entity.UserDirectoryReminder;
import com.linkbrary.domain.reminder.entity.UserLinkReminder;
import com.linkbrary.domain.user.entity.Member;
import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserDirectory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_directory_id")
    private Long id;

    @Column(nullable = false)
    private String directoryName;

    @Column(nullable = true)
    private boolean isAlarmed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_folder_id")
    private UserDirectory parentFolder;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserDirectory> childFolders;

    @OneToMany(mappedBy = "userDirectory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLink> userLinks;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userDirectory")
    private UserDirectoryReminder userDirectoryReminder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void updateParentFolder(UserDirectory parentFolder) {
        this.parentFolder = parentFolder;
    }

    public void updateDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public void updateIsAlarmedFalse() {this.isAlarmed = false;}
    public void updateIsAlarmedTrue() {this.isAlarmed = true;}
}
