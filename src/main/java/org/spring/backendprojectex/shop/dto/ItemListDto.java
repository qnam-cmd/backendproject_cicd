package org.spring.backendprojectex.shop.dto;

import lombok.*;
import org.spring.backendprojectex.shop.entity.CartEntity;
import org.spring.backendprojectex.shop.entity.ItemEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemListDto {
    private Long id;

    private int itemSize;

    private CartEntity cartEntity;

    private ItemEntity itemEntity;

    private String newFileName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
