package org.spring.backendprojectex.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.spring.backendprojectex.shop.dto.ItemReplyDto;
import org.spring.backendprojectex.shop.entity.ItemEntity;
import org.spring.backendprojectex.shop.entity.ItemReplyEntity;
import org.spring.backendprojectex.shop.repository.ItemReplyRepository;
import org.spring.backendprojectex.shop.repository.ItemRepository;
import org.spring.backendprojectex.shop.service.ItemReplyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemReplyServiceImpl implements ItemReplyService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemReplyRepository itemReplyRepository;

    // ========================     상품댓글 등록     ==========================//
    @Override
    public void insertItemReply(ItemReplyDto itemReplyDto) {
        // 상품 확인
        ItemEntity item = itemRepository.findById(itemReplyDto.getItemEntity().getId())
                .orElseThrow(()-> new IllegalArgumentException("상품(id)이 존재하지 않습니다."));
        // 회원 확인
        MemberEntity member = memberRepository.findById(itemReplyDto.getMemberEntity().getId())
                .orElseThrow(()-> new IllegalArgumentException("회원(id)이 존재하지 않습니다."));
        // DTO -> Entity
        ItemReplyEntity itemReplyEntity = ItemReplyEntity.builder()
                .itemReplyWriter(itemReplyDto.getItemReplyWriter())
                .itemReplyContent(itemReplyDto.getItemReplyContent())
                .itemReplyPw(itemReplyDto.getItemReplyPw())
                .memberEntity(member)
                .itemEntity(item)
                .build();
        // 저장
        itemReplyRepository.save(itemReplyEntity);

    }

    // ========================     상품댓글 (전체)목록     ==========================//
    @Override
    public List<ItemReplyDto> itemReplyList(Long itemId) {

        List<ItemReplyEntity> itemReplyEntities = itemReplyRepository.findByItemEntity_Id(itemId);
        return itemReplyEntities.stream().map(reply ->
                        ItemReplyDto.builder()
                                .itemReplyWriter(reply.getItemReplyWriter())
                                .itemReplyContent(reply.getItemReplyContent())
                                .itemReplyPw(reply.getItemReplyPw())
                                .memberEntity(reply.getMemberEntity())
                                .itemEntity(reply.getItemEntity())
                                .build())
                .toList();
    }

    // ========================     상품댓글 (상세)목록     ==========================//
    @Override
    public ItemReplyDto itemReplyDetail(Long id) {

        ItemReplyEntity reply = itemReplyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글(id)이 존재하지 않습니다."));

        return ItemReplyDto.builder()
                .id(reply.getId())
                .itemReplyWriter(reply.getItemReplyWriter())
                .itemReplyContent(reply.getItemReplyContent())
                .itemReplyPw(reply.getItemReplyPw())
                .memberEntity(reply.getMemberEntity())
                .itemEntity(reply.getItemEntity())
                .createTime(reply.getCreateTime())
                .updateTime(reply.getUpdateTime())
                .build();
    }
}
