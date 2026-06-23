package org.spring.backendprojectex.shop.repository;

import org.spring.backendprojectex.shop.entity.ItemReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemReplyRepository extends JpaRepository<ItemReplyEntity,Long> {

    List<ItemReplyEntity> findByItemEntity_Id(Long itemId);
}
