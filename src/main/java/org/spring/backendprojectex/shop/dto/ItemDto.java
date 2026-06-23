package org.spring.backendprojectex.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.spring.backendprojectex.shop.entity.ItemFileEntity;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


// @Data = @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class ItemDto {
    private Long id;
    @NotBlank(message = "상품명을 입력해주세요")
    private String itemTitle;       // 상품명

    @NotBlank(message = "상품 설명을 입력해주세요")
    private String itemDetail;      // 상품 설명

    @NotNull(message = "상품 가격을 입력해주세요")
    private int itemPrice;          // 상품가격

    // 파일이미지
    private int attachFile;         // 파일 유무(1,0)

    // 파일 업로드 파일 저장할수있는 객체
    private MultipartFile  itemFile;
    private List<ItemFileEntity> fileEntities;
    private String oldFileName;
    private String newFileName;

    // View -> thymeleaf, react
    private Long memberId;
    private int itemSize;   // ItemList itemSize
    private MemberEntity memberEntity;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
