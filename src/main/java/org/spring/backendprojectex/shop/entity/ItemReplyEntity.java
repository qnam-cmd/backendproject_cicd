package org.spring.backendprojectex.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.spring.backendprojectex.common.BasicTime;
import org.spring.backendprojectex.member.entity.MemberEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "item_reply_tb06")
public class ItemReplyEntity extends BasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_reply_id")
    private Long id;

    @Column(nullable = false)
    private String itemReplyWriter;

    @Column(nullable = false)
    private String itemReplyContent;

    @Column(nullable = false)
    private String itemReplyPw;

    // N:1 itemReplyEntity : itemEntity
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity itemEntity;

    // N:1 itemReplyEntity : memberEntity
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

}
