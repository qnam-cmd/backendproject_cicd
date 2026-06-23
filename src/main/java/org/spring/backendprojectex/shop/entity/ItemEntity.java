package org.spring.backendprojectex.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.spring.backendprojectex.common.BasicTime;
import org.spring.backendprojectex.member.entity.MemberEntity;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "item_tb06")
public class ItemEntity extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false, unique = false)
    private String itemTitle;       // 상품명

    @Column(nullable = false)
    private String itemDetail;      // 상품 설명

    @Column(nullable = false)
    private int itemPrice;          // 상품가격

    @Column(nullable = false)
    private int attachFile;         // 파일 유무(1,0)

    //  N:1 (ItemEntity : MemberEntity)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    //  1:N (ItemEntity : ItemFileEntity)
    @JsonIgnore
    @OneToMany(mappedBy = "itemEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true) // 상품삭제 시 같이삭제
    private List<ItemFileEntity> itemFileEntities;

    // 1:N (ItemEntity : ItemReplyEntity)
    @JsonIgnore
    @OneToMany(mappedBy = "itemEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true) // 상품삭제 시 같이삭제
    private List<ItemReplyEntity> itemReplyEntities;

    // 1:N
    @JsonIgnore
    @OneToMany(mappedBy = "itemEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ItemListEntity> itemListEntities;

}
