package com.linkbrary.domain.link.entity;

import jakarta.persistence.*;
import java.util.List;

import com.linkbrary.common.entity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Link extends BaseTimeEntity {

    @Id
    @Column(name = "link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "embedding")
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 3072) // dimensions
    private float[] embedding;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 255)
    private String title;

    private String thumbnail;

    @OneToMany(mappedBy = "link", cascade = CascadeType.ALL)
    private List<UserLink> userLinks;
}
