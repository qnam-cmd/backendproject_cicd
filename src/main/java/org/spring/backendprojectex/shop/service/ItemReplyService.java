package org.spring.backendprojectex.shop.service;

import org.spring.backendprojectex.shop.dto.ItemReplyDto;

import java.util.List;

public interface ItemReplyService {
    // 상품댓글 등록
    void insertItemReply(ItemReplyDto itemReplyDto);
    // 상품댓글 (전체)목록
    List<ItemReplyDto> itemReplyList(Long itemId);
    // 상품댓글 (상세)목록
    ItemReplyDto itemReplyDetail(Long id);

}
