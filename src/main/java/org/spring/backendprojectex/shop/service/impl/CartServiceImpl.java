package org.spring.backendprojectex.shop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.shop.dto.ItemListDto;
import org.spring.backendprojectex.shop.entity.CartEntity;
import org.spring.backendprojectex.shop.entity.ItemEntity;
import org.spring.backendprojectex.shop.entity.ItemListEntity;
import org.spring.backendprojectex.shop.repository.CartRepository;
import org.spring.backendprojectex.shop.repository.ItemListRepository;
import org.spring.backendprojectex.shop.repository.ItemRepository;
import org.spring.backendprojectex.shop.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CartServiceImpl implements CartService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final ItemListRepository itemListRepository;


    @Transactional
    @Override
    public void insertCart(Long memberId, Long id) {
        // 1. 회원, 상품 확인
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원(memberId)이 없습니다"));
        ItemEntity itemEntity = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품(id)이 없습니다."));
        // 2. 장바구니 확인 (없으면 생성)
        CartEntity cartEntity = cartRepository.findByMemberEntityId(memberEntity.getId())
                // 조회후, 없으면 새로만들어서 저장한뒤 가져온다 JPA패턴
                .orElseGet(() -> cartRepository.save(CartEntity.builder().memberEntity(memberEntity).build()));
        // 3. 장바구니 && 상품 -> 상품목록에 동시에 존재하는지 확인
        List<ItemListEntity> itemListEntities = itemListRepository.findByCartEntityIdAndItemEntityId(cartEntity.getId(), itemEntity.getId());

        if (itemListEntities.isEmpty()) {
            // 처음 닫는 상품인 경우 새로 생성 및 저장
            ItemListEntity itemListEntity = ItemListEntity.builder()
                    .cartEntity(cartEntity)
                    .itemEntity(itemEntity)
                    .itemSize(1)    // 최초 수량1
                    .build();
            itemListRepository.save(itemListEntity);
        } else {
            // 이미 장바구니에 있는 물건일경우 수량+1 증가 (더티 체킹 활용)
            ItemListEntity existingItem = itemListEntities.get(0);   // 기존에 담겨있던 0번째 데이터를 가져온다. (어차피 중복된 값은 1개, 첫번째값가져오기)
            existingItem.setItemSize(existingItem.getItemSize() + 1); // 수량+1
            // 따로 끝나면 save() 호출을 안해도 트랜잭션이 끝나서 자동으로 DB에 update쿼리가 나간다.
        }
    }

    @Override
    @Transactional
    public void insertCart2(ItemDto itemDto) {
        // 1. 회원, 상품 확인
        MemberEntity memberEntity = memberRepository.findById(itemDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원(memberId)이 없습니다"));
        ItemEntity itemEntity = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("상품(id)이 없습니다."));
        // 2. 장바구니 확인 (없으면 생성)
        // 장바구니 있으면 재사용
        CartEntity cartEntity = cartRepository.findByMemberEntityId(memberEntity.getId())
                // 조회후, 없으면 새로만들어서 저장한뒤 가져온다 JPA패턴
                .orElseGet(() -> cartRepository.save(CartEntity.builder().memberEntity(memberEntity).build()));
        // 3. 장바구니 && 상품 -> 상품목록에 동시에 존재하는지 확인
        List<ItemListEntity> itemListEntities = itemListRepository.findByCartEntityIdAndItemEntityId(cartEntity.getId(), itemEntity.getId());

        if (itemListEntities.isEmpty()) {
            // 처음 닫는 상품인 경우 새로 생성 및 저장
            ItemListEntity itemListEntity = ItemListEntity.builder()
                    .cartEntity(cartEntity)
                    .itemEntity(itemEntity)
                    .itemSize(itemDto.getItemSize())    // 전달받은 수량만큼 설정
                    .build();
            itemListRepository.save(itemListEntity);
        } else {
            ItemListEntity existingItem = itemListEntities.get(0);
            // 이미 존재하는경우 수량을 덮어씌우거나
//            existingItem.setItemSize(itemDto.getItemSize());
            // 이미 존재하는 경우 수량을 더하기
            existingItem.setItemSize(itemDto.getItemSize() + existingItem.getItemSize());
            // 따로 끝나면 save() 호출을 안해도 트랜잭션이 끝나서 자동으로 DB에 update쿼리가 나간다.
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemListDto> cartList(Long memberId) {
        // 회원아이디 먼저확인
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원(id)이 없습니다."));
        // 장바구니 확인
        Optional<CartEntity> optionalCartEntity = cartRepository.findByMemberEntityId(memberEntity.getId());
        if (optionalCartEntity.isEmpty()) {
            return new ArrayList<>();
        }
        CartEntity cartEntity = optionalCartEntity.get();
        // 확인한 값(id)에 있는 ItemListEntity 데이터를 가져와 리스트타입으로 저장
        List<ItemListEntity> itemList = itemListRepository.findAllByCartEntityId(cartEntity.getId());
        // 빌더,맵 이용해서 화면에 보여주기 (DTO변환)
        return itemList.stream()
                .map(item -> {
                    String newFileName = null;
                    ItemEntity itemEntity = item.getItemEntity();
                    if (itemEntity.getAttachFile() == 1
                            && itemEntity.getItemFileEntities() != null
                            && !itemEntity.getItemFileEntities().isEmpty()) {
                        newFileName = itemEntity.getItemFileEntities().get(0).getNewFileName();
                    }

                    return ItemListDto.builder()
                            .id(item.getId())
                            .itemSize(item.getItemSize())
                            .cartEntity(item.getCartEntity())
                            .itemEntity(item.getItemEntity())
                            .newFileName(newFileName)           // itemListEntity에서 newFilename 선언후 가져오는게아니라, ItemEntity에서 가져와 담는다.
                            .createTime(item.getCreateTime())
                            .updateTime(item.getUpdateTime())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public int countCartItems(Long memberId) {
        return cartRepository.countByMemberEntity(MemberEntity.builder()
                .id(memberId)
                .build());
    }

    @Override
    public void clearCart(Long cartId) {
        cartRepository.findById(cartId)
                .ifPresent(cart -> {
                    cartRepository.delete(cart);    //객체
                    log.info("장바구니(ID: {}) 삭제 완료",cartId);
                });
    }
}
