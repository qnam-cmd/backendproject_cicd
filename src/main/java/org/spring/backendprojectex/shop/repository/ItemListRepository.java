package org.spring.backendprojectex.shop.repository;

import org.spring.backendprojectex.shop.entity.ItemListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemListRepository extends JpaRepository<ItemListEntity, Long> {
    List<ItemListEntity> findByCartEntityIdAndItemEntityId(Long id, Long id1);

    List<ItemListEntity> findAllByCartEntityId(Long id);
    int countByCartEntityId(Long id);
}
