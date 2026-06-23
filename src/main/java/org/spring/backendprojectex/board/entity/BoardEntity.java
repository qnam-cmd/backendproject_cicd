package org.spring.backendprojectex.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.spring.backendprojectex.common.BasicTime;
import org.spring.backendprojectex.member.entity.MemberEntity;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "board_tb06")
public class BoardEntity extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String boardTitle;       // 글 제목

    @Column(nullable = false)
    private String boardContent;     // 글 내용

    @Column(nullable = false)
    private String boardPw;     // 글비밀번호

    @Column(nullable = false)
    private String nickName;    //닉네임

    private int hit;            //조회수

    // 파일첨부 유무
    private int attachFile;

    //연관 관계
    // 외래키
    // BoardEntity N:1 MemberEntity
    // member_id -> 칼럼명 자동생성
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    // BoardEntity 1:N BoardReplyEntity
    @OneToMany(mappedBy = "boardEntity",
            cascade = CascadeType.REMOVE)
    private List<BoardReplyEntity> boardReplyEntities;

    // BoardEntity 1:N  BoardFileEntity
    @OneToMany(mappedBy = "boardEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    private List<BoardFileEntity> boardFileEntities;

}
