package org.spring.backendprojectex.member.service;

import org.spring.backendprojectex.member.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    void memberInsert(MemberDto memberDto);
    Page<MemberDto> pagingSearchMemberList(Pageable pageable, String subject, String search);

    MemberDto memberDetail(Long id);
}
