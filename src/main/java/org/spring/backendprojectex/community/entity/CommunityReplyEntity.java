package org.spring.backendprojectex.community.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.spring.backendprojectex.board.entity.BoardEntity;
import org.spring.backendprojectex.common.BasicTime;
import org.spring.backendprojectex.member.entity.MemberEntity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "community_reply_tb06")
public class CommunityReplyEntity extends BasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_reply_id")
    private Long id;

    @Column(nullable = false)
    private String replyContent;

    @Column(nullable = false)
    private String replyWriter; // 작성자

    @Column(nullable = false)
    private String replyPw;    // 댓글 비밀번호

    // N:1 CommunityEntity
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private CommunityEntity communityEntity;

    // N:1 MemberEntity
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

}
