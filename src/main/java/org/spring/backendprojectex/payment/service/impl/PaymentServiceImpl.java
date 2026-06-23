package org.spring.backendprojectex.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.spring.backendprojectex.payment.dto.PaymentDto;
import org.spring.backendprojectex.payment.dto.PaymentItemDto;
import org.spring.backendprojectex.shop.entity.CartEntity;
import org.spring.backendprojectex.shop.entity.ItemListEntity;
import org.spring.backendprojectex.payment.entity.PaymentEntity;
import org.spring.backendprojectex.payment.entity.PaymentItemEntity;
import org.spring.backendprojectex.shop.repository.CartRepository;
import org.spring.backendprojectex.shop.repository.ItemListRepository;
import org.spring.backendprojectex.payment.repository.PaymentItemRepository;
import org.spring.backendprojectex.payment.repository.PaymentRepository;
import org.spring.backendprojectex.payment.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentItemRepository paymentItemRepository;
    private final ItemListRepository itemListRepository;

    // =======================            결제 하기           ==============================//
    @Override
    @Transactional
    public void insertPayment(PaymentDto paymentDto) {
        // 회원정보 확인
        MemberEntity memberEntity = memberRepository.findById(paymentDto.getMemberId())
                .orElseThrow(()->new IllegalArgumentException("회원(id)이 없습니다."));
        // 장바구니 확인
        List<ItemListEntity> itemListEntities = itemListRepository.findAllByCartEntityId(paymentDto.getCartId());
        if (itemListEntities.isEmpty()) {
            throw new IllegalStateException("장바구니가 비어있어 주문을 진행 할 수 없습니다.");
        }
        // 결제정보(PaymentEntity) 저장
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .paymentType(paymentDto.getPaymentType())
                .orderMethod(paymentDto.getOrderMethod())
                .orderPost(paymentDto.getOrderPost())
                .payResult(paymentDto.getPayResult())
                .orderAddr(paymentDto.getOrderAddr())
                .memberEntity(memberEntity) // 영속 객체 매핑
                .build();
        PaymentEntity savedPayment = paymentRepository.save(paymentEntity);
        // 장바구니 내역을 결제 상세 내역(PaymentItemEntity) 스냅샷으로 변환(저장)
        List<PaymentItemEntity> paymentItemEntities = itemListEntities.stream().map(el ->
                        PaymentItemEntity.builder()
                                .paymentItemTitle(el.getItemEntity().getItemTitle())
                                .paymentItemPrice(el.getItemEntity().getItemPrice())
                                .paymentItemSize(el.getItemSize())
                                .paymentEntity(savedPayment)    // 완벽히 영속화된 결제 마스터 객체 매핑
                                .build())
                .toList();
        // 결제 상세 상품 대량 저장 (Bulk Insert 효과 -> DB 비용을 줄일 수 있다.)
        paymentItemRepository.saveAll(paymentItemEntities);
        // 장바구니 비우기 (결제 완료 시점에 실행)
        CartEntity cartEntity = cartRepository.findByMemberEntityId(paymentDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("삭제할 장바구니가 없습니다."));
        cartRepository.delete(cartEntity);

    }


    // =======================            특정회원(ID) 결제내역 (전체)조회           ==============================//
    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDto> paymentList(Pageable pageable, String subject, String search, Long memberId) {
        Page<PaymentEntity> paymentPage;

        // 1. 검색어가 없거나 조건이 없으면 해당 회원의 전체 내역 조회
        if (subject == null || search == null || search.trim().isEmpty()) {
            paymentPage = paymentRepository.findAllByMemberEntityIdOrderByIdDesc(memberId, pageable);
        } else {
            // 2. 검색 조건에 따른 switch 분기 (memberId를 반드시 포함해야 함)
            paymentPage = switch (subject) {
                case "paymentType" ->
                        paymentRepository.findByMemberEntityIdAndPaymentTypeContaining(memberId, search, pageable);
                case "orderPost" ->
                        paymentRepository.findByMemberEntityIdAndOrderPostContaining(memberId, search, pageable);
                case "orderAddr" ->
                        paymentRepository.findByMemberEntityIdAndOrderAddrContaining(memberId, search, pageable);
                case "payResult" ->
                        paymentRepository.findByMemberEntityIdAndPayResultContaining(memberId, search, pageable);
                case "orderMethod" ->
                        paymentRepository.findByMemberEntityIdAndOrderMethodContaining(memberId, search, pageable);
                default -> paymentRepository.findAllByMemberEntityIdOrderByIdDesc(memberId, pageable);
            };
        }

        // 3. Page<Entity> -> Page<Dto> 변환
        return paymentPage.map(el -> PaymentDto.builder()
                .id(el.getId())
                .paymentType(el.getPaymentType())
                .orderAddr(el.getOrderAddr())
                .orderPost(el.getOrderPost())
                .orderMethod(el.getOrderMethod())
                .payResult(el.getPayResult())
                .createTime(el.getCreateTime())
                .updateTime(el.getUpdateTime())
                .memberId(el.getMemberEntity().getId())
                .paymentItemEntities(el.getPaymentItemEntities())
                .build());
    }

    // =======================            특정회원(ID) 결제내역 (상세)조회           ==============================//
    @Override
    public PaymentDto findPaymentById(Long id) {
        PaymentEntity paymentEntity = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("아이디가 없습니다: " + id));
        // 상품 엔티티 리스트를 상품 DTO 리스트로 변환
        List<PaymentItemDto> itemDtos = paymentEntity.getPaymentItemEntities().stream()
                .map(item -> PaymentItemDto.builder()
                        .id(item.getId())
                        .paymentItemTitle(item.getPaymentItemTitle())
                        .paymentItemPrice(item.getPaymentItemPrice())
                        .paymentItemSize(item.getPaymentItemSize())
                        .build())
                .toList();

        return PaymentDto.builder()
                .id(paymentEntity.getId())
                .paymentType(paymentEntity.getPaymentType())
                .orderMethod(paymentEntity.getOrderMethod())
                .orderPost(paymentEntity.getOrderPost())
                .payResult(paymentEntity.getPayResult())
                .orderAddr(paymentEntity.getOrderAddr())
                .memberId(paymentEntity.getMemberEntity().getId())
                .createTime(paymentEntity.getCreateTime())
                .updateTime(paymentEntity.getUpdateTime())
                .paymentItemEntities(paymentEntity.getPaymentItemEntities())
                .build();
    }

    // =======================            결제내역 (전체)조회           ==============================//
    @Override
    public Page<PaymentDto> paymentAllList(Pageable pageable,String subject, String search) {
        Page<PaymentEntity> paymentAllPage = null;
        if(subject==null || search == null || search.length()<=0) {
            paymentAllPage = paymentRepository.findAll(pageable);
        } else {
// 2. 검색 조건(subject)에 따른 switch 분기 처리
            switch (subject) {
                case "paymentType":
                    paymentAllPage = paymentRepository.findByPaymentTypeContaining(search, pageable);
                    break;
                case "orderPost":
                    paymentAllPage = paymentRepository.findByOrderPostContaining(search, pageable);
                    break;
                case "orderAddr":
                    paymentAllPage = paymentRepository.findByOrderAddrContaining(search, pageable);
                    break;
                case "payResult":
                    paymentAllPage = paymentRepository.findByPayResultContaining(search, pageable);
                    break;
                case "orderMethod":
                    paymentAllPage = paymentRepository.findByOrderMethodContaining(search, pageable);
                    break;
                case "memberId":
                    try {
                        Long searchMemberId = Long.valueOf(search);
                        paymentAllPage = paymentRepository.findByMemberEntityId(searchMemberId, pageable);
                    } catch (NumberFormatException e) {
                        // memberId에 숫자가 아닌 문자를 입력하고 검색했을 경우 빈 페이지 반환
                        paymentAllPage = Page.empty(pageable);
                    }
                    break;
                default:
                    paymentAllPage = paymentRepository.findAll(pageable);
                    break;
            }
        }

        // 3. 조회된 Page<Entity>를 Page<Dto>로 일괄 변환하여 리턴
        return paymentAllPage.map(el -> PaymentDto.builder()
                .id(el.getId())
                .paymentType(el.getPaymentType())
                .orderMethod(el.getOrderMethod())
                .orderPost(el.getOrderPost())
                .orderAddr(el.getOrderAddr())
                .payResult(el.getPayResult())
                .memberId(el.getMemberEntity().getId())
                .createTime(el.getCreateTime())
                .updateTime(el.getUpdateTime())
                .paymentItemEntities(el.getPaymentItemEntities())
                .build()
        );
    }



}
