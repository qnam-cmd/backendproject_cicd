package org.spring.backendprojectex.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.common.Role;
import org.spring.backendprojectex.member.dto.MemberDto;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.spring.backendprojectex.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    // Bean으로 등록
    private final PasswordEncoder passwordEncoder;


    // =========================    회원 가입    ==================================//
    @Override
    public void memberInsert(MemberDto memberDto) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUserEmail(memberDto.getUserEmail());
        if (optionalMemberEntity.isPresent()) {
            throw new IllegalArgumentException("이메일이 존재합니다");
        }
        memberRepository.save(MemberEntity.builder()
                .userEmail(memberDto.getUserEmail())
                .userPw(passwordEncoder.encode(memberDto.getUserPw()))
                .userName(memberDto.getUserName())
                .role(Role.MEMBER)
                .build());
    }
    // ============================    회원 수정    ==================================//
    // ============================    회원 삭제    ==================================//



    // =========================    회원 (전체,페이징)조회    ==================================//

    @Override
    public Page<MemberDto> pagingSearchMemberList(Pageable pageable, String subject, String search) {
        Page<MemberEntity> memberEntities = null;

        if (subject == null || search == null || search.isBlank()) {
            memberEntities = memberRepository.findAll(pageable);
        } else {
            memberEntities = switch (subject) {
                case "userEmail" -> memberRepository.findByUserEmailContaining(pageable, search);
                case "userName" -> memberRepository.findByUserNameContaining(pageable, search);
                default -> memberRepository.findAll(pageable);
            };
        }
        return memberEntities.map(member -> MemberDto.builder()
                .id(member.getId())
                .userEmail(member.getUserEmail())
                .userName(member.getUserName())
                .role(member.getRole())
                .createTime(member.getCreateTime())
                .updateTime(member.getUpdateTime())
                .build());
    }
    // =========================    회원 (상세)조회    ==================================//

    @Override
    public MemberDto memberDetail(Long id) {
        MemberEntity memberEntity = memberRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("회원(id)이 존재하지않습니다"+id));

        return MemberDto.builder()
                .id(memberEntity.getId())
                .userEmail(memberEntity.getUserEmail())
                .userName(memberEntity.getUserName())
                .userPw(memberEntity.getUserPw())
                .role(memberEntity.getRole())
                .updateTime(memberEntity.getUpdateTime())
                .createTime(memberEntity.getCreateTime())
                .build();

    }

}
