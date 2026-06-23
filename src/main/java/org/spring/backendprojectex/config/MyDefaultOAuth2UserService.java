package org.spring.backendprojectex.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.common.Role;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MyDefaultOAuth2UserService extends DefaultOAuth2UserService {


    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        ClientRegistration clientRegistration = userRequest.getClientRegistration();    //사용자 정보
        String registrationId = clientRegistration.getRegistrationId();        // google,kakao,naver

        log.info("==========oAuth2User===========");
        log.info(oAuth2User);
        log.info("==========clientRegistration===========");
        log.info(clientRegistration);
        log.info("==========registrationId===========");
        log.info(registrationId);

        //로그인사용자, (구글,카카오,네이버)
        return oAuth2UserSuccess(oAuth2User, registrationId);
    }

    private OAuth2User oAuth2UserSuccess(OAuth2User oAuth2User, String registrationId) {
        String userEmail = "";
        String userName = "";
        String userPassword = "";

        if (registrationId.equals("google")) {
            userEmail = oAuth2User.getAttribute("email");
            userName = oAuth2User.getAttribute("name");
        } else if (registrationId.equals("naver")) {
            Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
            userEmail = (String) response.get("email");
            userName = (String) response.get("name");
        } else if (registrationId.equals("kakao")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
            userEmail = (String) kakaoAccount.get("email");
            userName = (String) kakaoProfile.get("nickname");
        }
        // kakao, naver 로직생략
        if (userEmail == null || userEmail.isEmpty()) {
            throw new OAuth2AuthenticationException("이메일 정보가 없습니다.");
        }

        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUserEmail(userEmail);
        //  등록되어있으면
        if (optionalMemberEntity.isPresent()) {
            // 기존회원관리
            return new MyUserDetails(optionalMemberEntity.get());
        }
        // 처음 SNS회원 등록
        userPassword = passwordEncoder.encode("임시비밀번호1234");

        MemberEntity memberEntity = memberRepository.save(
                MemberEntity.builder()
                        .userEmail(userEmail)
                        .userPw(userPassword)
                        .userName(userName)
                        .role(Role.MEMBER)
                        .build()
        );
        return new MyUserDetails(memberEntity, oAuth2User.getAttributes());
    }


}
