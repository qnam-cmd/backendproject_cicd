package org.spring.backendprojectex;

import org.junit.jupiter.api.Test;
import org.spring.backendprojectex.common.Role;
import org.spring.backendprojectex.community.entity.CommunityEntity;
import org.spring.backendprojectex.community.repository.CommunityRepository;
import org.spring.backendprojectex.member.dto.MemberDto;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.spring.backendprojectex.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class MemberTest {
    //===========================   회원 테스트   ========================================//

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void insert() {
        for(int i=0; i<10; i++) {
            memberRepository.save(MemberEntity.builder()
                            .userEmail("m"+i+"@email.com")
                            .userPw(passwordEncoder.encode("111"))
                            .userName("m"+i)
                            .role(Role.MEMBER)
                    .build());
        }
    }

    //===========================   게시글 테스트   ========================================//

    @Autowired
    CommunityRepository communityRepository;

    @Test
    void insertCommunity() {
        // 공지사항 1
        // Q&A  2
        // 후기   3
        for(int i=0; i<10; i++) {
            communityRepository.save(CommunityEntity.builder()
                            .count(0)
                            .title("제목"+i)
                            .content("내용"+i)
                            .nickName("닉네임"+i)
                            .category("공지사항")
                            .memberEntity(MemberEntity.builder()
                                    .id(1L)
                                    .build())
                    .build());
        }
    }
}
