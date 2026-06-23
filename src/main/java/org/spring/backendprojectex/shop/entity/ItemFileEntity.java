package org.spring.backendprojectex.shop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spring.backendprojectex.common.BasicTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "item_file_tb06")
public class ItemFileEntity extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_file_id")
    private Long id;

    @Column(nullable = false)
    private String oldFileName;

    @Column(nullable = false)
    private String newFileName;

    // N:1 (ItemFileEntity : ItemEntity)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity itemEntity;

}
