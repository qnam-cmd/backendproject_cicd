package org.spring.backendprojectex.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.shop.entity.ItemFileEntity;
import org.spring.backendprojectex.shop.entity.ItemEntity;
import org.spring.backendprojectex.shop.repository.ItemFileRepository;
import org.spring.backendprojectex.shop.repository.ItemRepository;
import org.spring.backendprojectex.shop.service.ItemService;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    @Value("${img.path.item}")
    private String itemUploadPath;

    private final ItemRepository itemRepository;
    private final ItemFileRepository itemFileRepository;
    private final MemberRepository memberRepository;

    // =========================         상품 (전체)목록        ==================================== //
    @Override
    public List<ItemDto> itemListFn() {
        return itemRepository.findAll().stream().map(item ->
                        ItemDto.builder()
                                .id(item.getId())
                                .itemTitle(item.getItemTitle())
                                .itemDetail(item.getItemDetail())
                                .itemPrice(item.getItemPrice())
                                .memberId(item.getMemberEntity().getId())
                                .attachFile(item.getAttachFile())
                                .createTime(item.getCreateTime())
                                .updateTime(item.getUpdateTime())
                                .newFileName(item.getItemFileEntities().get(0).getNewFileName())
                                .oldFileName(item.getItemFileEntities().get(0).getOldFileName())
                                .build())
                .toList();
    }

    // =========================         상품 목록        ==================================== //
    @Override
    public ItemDto oneItemFn(Long id) {
        return null;
    }

    // =========================         상품 검색(페이징)        ==================================== //
    @Override
    public Page<ItemDto> pagingSearchItemFn(Pageable pageable, String subject, String search) {

        // 검색조건 처리
        Page<ItemEntity> itemEntities = null;

        if (subject == null || search == null || search.isBlank()) {
            // 검색창이 null, 비어있으면(isBlank) 목록전체 리턴
            itemEntities = itemRepository.findAll(pageable);
        } else {
            itemEntities = switch (subject) {
                case "itemTitle" -> itemRepository.findByItemTitleContaining(pageable, search);
                case "itemDetail" -> itemRepository.findByItemDetailContaining(pageable, search);
                default -> itemRepository.findAll(pageable);
            };
        }
        return itemEntities.map(item ->
            ItemDto.builder()
                    .id(item.getId())
                    .memberId(item.getId())
                    .itemTitle(item.getItemTitle())
                    .itemDetail(item.getItemDetail())
                    .itemPrice(item.getItemPrice())
                    .memberId(item.getMemberEntity().getId())
                    .attachFile(item.getAttachFile())
                    .createTime(item.getCreateTime())
                    .updateTime(item.getUpdateTime())
                    .newFileName(item.getItemFileEntities().get(0).getNewFileName())
                    .oldFileName(item.getItemFileEntities().get(0).getOldFileName())
                    .build()
        );
    }

    // =========================         상품 등록        ==================================== //
    @Override
    public void insertItem(ItemDto itemDto) throws IOException {
        // 1. 작성자
        MemberEntity memberEntity = memberRepository.findById(itemDto.getMemberId())
                .orElseThrow(()-> new IllegalArgumentException("회원(id)이 존재하지 않습니다."));
        // 2. 파일 없는경우
        if (itemDto.getItemFile().isEmpty()) {
            ItemEntity itemEntity = ItemEntity.builder()
                    .memberEntity(memberEntity)
                    .attachFile(0)
                    .itemDetail(itemDto.getItemDetail())
                    .itemPrice(itemDto.getItemPrice())
                    .itemTitle(itemDto.getItemTitle())
                    .build();
            itemRepository.save(itemEntity);
            return;
        }
        // 3. 파일 있는경우
        MultipartFile itemFile = itemDto.getItemFile();
        String oldFileName = itemFile.getOriginalFilename();
        String newFileName = UUID.randomUUID() + "_" + oldFileName;
        String fileSavePath = itemUploadPath + newFileName;
        File saveFile = new File(fileSavePath);

        // 저장폴더없으면 폴더먼저생성
        if(!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }
        // 파일 저장
        itemFile.transferTo(saveFile);
        // 게시글 저장
        ItemEntity itemEntity = ItemEntity.builder()
                .memberEntity(memberEntity)
                .attachFile(1)
                .itemTitle(itemDto.getItemTitle())
                .itemPrice(itemDto.getItemPrice())
                .itemDetail(itemDto.getItemDetail())
                .build();
        ItemEntity saveItem = itemRepository.save(itemEntity);
        // 파일 DB저장
        itemFileRepository.save(ItemFileEntity.builder()
                .itemEntity(saveItem)
                .oldFileName(oldFileName)
                .newFileName(newFileName)
                .build());
    }

    // =========================         상품 상세 목록        ==================================== //
    @Override
    public ItemDto itemDetailFn(Long id) {
        // 1. 상품조회 (없을 경우 명확한 예외 메시지 처리)
        ItemEntity itemEntity = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을수가없습니다" + id));
        // 2. 상세 페이지 상품 수량 기본값설정
        // 타인들의 장바구니 내역(getItemListEntities)을 조회하는 불필요한 로직과 추가쿼리를 제거하고 기본값 1로 세팅한다.
        int defaultSize = 1;

        return ItemDto.builder()
                .id(itemEntity.getId())
                .itemTitle(itemEntity.getItemTitle())
                .itemDetail(itemEntity.getItemDetail())
                .itemPrice(itemEntity.getItemPrice())
                .itemSize(defaultSize)
                .memberId(itemEntity.getMemberEntity().getId())
                .createTime(itemEntity.getCreateTime())
                .updateTime(itemEntity.getUpdateTime())
                .attachFile(itemEntity.getAttachFile())
                .newFileName(itemEntity.getItemFileEntities().get(0).getNewFileName())
                .oldFileName(itemEntity.getItemFileEntities().get(0).getOldFileName())
                .build();
    }
}
