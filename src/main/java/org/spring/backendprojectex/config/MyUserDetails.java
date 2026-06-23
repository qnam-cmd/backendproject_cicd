package org.spring.backendprojectex.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class MyUserDetails implements UserDetails, OAuth2User {

    // 필드는 위에 (필드, 생성자, 메서드 순)

    // =======================                    1. 필드                    ====================================== //

    // 로그인 사용자 (일반/SNS 공통)
    private MemberEntity memberEntity;
    // OAuth2.0 로그인관리 (SNS)
    private Map<String , Object> getAttributes;

    // ============================        2. 생성자(클래스명과 동일한 메서드)      ====================================== //

    // 일반 로그인
    public MyUserDetails(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }
    // OAuth2 로그인 관리 (SNS계정 로그인)
    public MyUserDetails(MemberEntity memberEntity, Map<String, Object> getAttributes) {
        this.memberEntity=memberEntity;
        this.getAttributes=getAttributes;
    }


    // =======================                    3. 메서드                    ====================================== //

    // ----------------  UserDetails 인터페이스 구현 메서드 --------------------------//
    // OAuth2.0 인터페이스 구현 메서드

    // 권한 설정
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자권한 리스트(객체 생성)
        Collection<GrantedAuthority> collectRoles = new ArrayList<>();
        // 문자열 처리
        collectRoles.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_" + memberEntity.getRole().toString(); // ROLE_MEMBER
            }
        });
        return collectRoles;
    }

    @Override
    public String getName() {
        return memberEntity.getUserName();
    }

    @Override
    public String getPassword() {
        return memberEntity.getUserPw();
    }

    @Override
    public String getUsername() {
        return memberEntity.getUserEmail();
    }

    // true -> 항상 사용가능
    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;    // 휴면계정 처리 false반환
    }
    // 계정 잠금여부
    @Override
    public boolean isAccountNonLocked() {
        return true;    // 비밀번호 n회 틀릴시 잠금 false반환
    }
    // 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;    // 비밀번호를 변경한지 n일 지났습니다. false반환시 예외처리,로그인차단,페이지이동,,,
    }
    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;    // 이메일인증 여부, 관리자 강제탈퇴시 false반환
    }

    // ----------------- OAuth2.0 사용자 인터페이스 구현 메서드 ------------------//

    // OAuth2.0
    @Override
    public Map<String, Object> getAttributes() {
        return getAttributes;
    }


    // -------------- 커스텀 메서드 ------------------------//
    public Long getId() {
        return memberEntity.getId();
    }

}
