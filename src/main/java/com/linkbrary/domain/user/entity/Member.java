package com.linkbrary.domain.user.entity;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.*;

import com.linkbrary.common.entity.BaseTimeEntity;
import com.linkbrary.domain.directory.entity.UserDirectory;
import com.linkbrary.domain.reminder.entity.UserDirectoryReminder;
import com.linkbrary.domain.reminder.entity.UserLinkReminder;
import com.linkbrary.domain.reminder.entity.UserReminderSetting;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email; // 이메일

    @Column(nullable = false)
    private String password;// 비밀번호

    @Column(nullable = false)
    private String nickname; // 닉네임

    @Column(nullable = false)
    private boolean push_notification;

    private String token;

    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<String> roles = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private UserReminderSetting userReminderSetting;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<UserDirectory> userDirectories;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<UserLinkReminder> userLinkReminders;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<UserDirectoryReminder> userDirectoryReminders;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void updateToken(String token) {
        this.token = token;
    }


    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }


}
