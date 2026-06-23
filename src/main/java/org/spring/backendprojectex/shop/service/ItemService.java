package org.spring.backendprojectex.shop.service;

import jakarta.validation.Valid;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ItemService {

    List<ItemDto> itemListFn();
    ItemDto oneItemFn(Long id);
    Page<ItemDto> pagingSearchItemFn(Pageable pageable, String subject, String search);
    void insertItem(@Valid ItemDto itemDto) throws IOException;
    ItemDto itemDetailFn(Long id);


}
