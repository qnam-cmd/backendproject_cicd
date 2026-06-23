package org.spring.backendprojectex.shop.repository;

import org.spring.backendprojectex.shop.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity,Long> {
    Page<ItemEntity> findByItemTitleContaining(Pageable pageable, String search);

    Page<ItemEntity> findByItemDetailContaining(Pageable pageable, String search);
}
