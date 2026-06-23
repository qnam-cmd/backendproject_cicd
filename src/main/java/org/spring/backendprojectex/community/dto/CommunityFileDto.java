package org.spring.backendprojectex.community.dto;
import lombok.*;
import org.spring.backendprojectex.community.entity.CommunityEntity;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityFileDto {

    private Long id;

    private String newFileName; // 새이름 -> DB, 로컬 저장소 저장이름

    private String oldFileName; // 파일 원래이름

    private CommunityEntity communityEntity;

    private Long communityId;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
