package org.spring.backendprojectex.community.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.community.servicce.CommunityService;
import org.spring.backendprojectex.config.MyUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/community")
public class CommunityRestApiController {

    private final CommunityService communityService;


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Integer> delete(@PathVariable("id") Long id,
                                          @AuthenticationPrincipal MyUserDetails myUserDetails) {
        // 1. 로그인 여부 확인
        if (myUserDetails == null) return ResponseEntity.status(401).build();

        communityService.deleteCommunity(id, myUserDetails.getId());

        return ResponseEntity.ok(1);
    }

}
