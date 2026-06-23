package org.spring.backendprojectex.shop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.shop.entity.ItemEntity;

import java.time.LocalDateTime;

// @Data = @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemReplyDto {

    private Long id;

    @NotBlank(message = "작성자를 입력해주세요.")
    private String itemReplyWriter;

    @NotBlank(message = "내용을 입력해주세요.")
    private String itemReplyContent;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String itemReplyPw;

    private ItemEntity itemEntity;

    private MemberEntity memberEntity;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
