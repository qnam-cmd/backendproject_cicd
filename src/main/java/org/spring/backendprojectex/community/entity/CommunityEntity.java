package org.spring.backendprojectex.community.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.spring.backendprojectex.common.BasicTime;
import org.spring.backendprojectex.member.entity.MemberEntity;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "community_tb06")
public class CommunityEntity extends BasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    public Long id;

    @Column(nullable = false)
    private String title;

    // 65535 -> 64kb
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String category;    // 공지사항,질문,Q&A,,,

    private int count;  // 조회수

    // 파일첨부 유무
    private int attachFile;

    // N:1
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    // 1:N
    // CommunityEntity 1:N CommunityReplyEntity
    @OneToMany(mappedBy = "communityEntity", cascade = CascadeType.REMOVE)
    private List<CommunityReplyEntity> communityReplyEntities;

    // CommunityEntity 1:N  CommunityFileEntity
    @OneToMany(mappedBy = "communityEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CommunityFileEntity> communityFileEntities;


}
