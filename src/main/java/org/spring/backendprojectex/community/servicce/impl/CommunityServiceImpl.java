package org.spring.backendprojectex.community.servicce.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.community.dto.CommunityDto;
import org.spring.backendprojectex.community.entity.CommunityEntity;
import org.spring.backendprojectex.community.entity.CommunityFileEntity;
import org.spring.backendprojectex.community.repository.CommunityFileRepository;
import org.spring.backendprojectex.community.repository.CommunityReplyRepository;
import org.spring.backendprojectex.community.repository.CommunityRepository;
import org.spring.backendprojectex.community.servicce.CommunityService;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityServiceImpl implements CommunityService {

    @Value("${img.path.board}")
    private String boardUploadPath;

    private final CommunityRepository communityRepository;
    private final CommunityFileRepository communityFileRepository;
    private final CommunityReplyRepository communityReplyRepository;
    private final MemberRepository memberRepository;


    // ======================================     게시글 작성     ==========================================//
    @Override
    public void communityInsert(CommunityDto communityDto) throws IOException {
        // 1. 작성자
        MemberEntity memberEntity = memberRepository.findById(communityDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원(id)이 존재하지 않습니다."));

        // 파일 여부
        MultipartFile communityFile = communityDto.getCommunityFile();

        // 2. 파일 없는경우
        if (communityFile == null || communityFile.isEmpty()) {
            CommunityEntity communityEntity = CommunityEntity.builder()
                    .memberEntity(memberEntity)
                    .attachFile(0)
                    .title(communityDto.getTitle())
                    .content(communityDto.getContent())
                    .nickName(communityDto.getNickName())
                    .category(communityDto.getCategory())
                    .count(0)
                    .build();
            communityRepository.save(communityEntity);
            return;
        }

        // 3. 파일 있는경우
        String oldFileName = communityFile.getOriginalFilename();
        String newFileName = UUID.randomUUID() + "_" + oldFileName;
        String fileSavePath = boardUploadPath + newFileName;
        File saveFile = new File(fileSavePath);

        // 저장폴더없으면 폴더먼저생성
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }
        // 파일 저장
        communityFile.transferTo(saveFile);
        // 게시글 저장
        CommunityEntity communityEntity = CommunityEntity.builder()
                .memberEntity(memberEntity)
                .attachFile(1)
                .title(communityDto.getTitle())
                .content(communityDto.getContent())
                .nickName(communityDto.getNickName())
                .category(communityDto.getCategory())
                .count(0)
                .build();
        CommunityEntity save = communityRepository.save(communityEntity);
        // 파일 DB저장
        communityFileRepository.save(CommunityFileEntity.builder()
                        .communityEntity(save)
                        .newFileName(newFileName)
                        .oldFileName(oldFileName)
                .build());
    }
    // ======================================     게시글 삭제     ==========================================//

    @Override
    public void deleteCommunity(Long id, Long memberId) {

        MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow(()-> new IllegalArgumentException("회원(id)이 없습니다."));
        CommunityEntity communityEntity = communityRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("게시글(id)이 없습니다."));

        // 작성자 검증
        if (!communityEntity.getMemberEntity().getId().equals(memberEntity.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        communityRepository.delete(communityEntity);
    }

    // ======================================     게시글 목록(검색,페이징)     ==========================================//
    @Override
    public Page<CommunityDto> communityList(Pageable pageable, String category , String subject, String search) {
        Page<CommunityEntity> communityEntities;

        // 카테고리,검색 사용 여부 (사용은 true(1), 아니면 false(0))
        boolean hasCategory = category != null && !category.isBlank();
        boolean hasSearch = search != null && !search.isBlank();

        // 검색 X
        if(!hasSearch) {
            communityEntities = hasCategory
                    ? communityRepository.findByCategory(category,pageable) // 참    카테고리 O
                    : communityRepository.findAll(pageable);                // 거짓   카테고리 X
        } else {

            // 검색 O
            communityEntities = switch (subject) {
                case "title" -> hasCategory
                        ? communityRepository.findByCategoryAndTitleContaining(category, search, pageable)  // 카테고리 O
                        : communityRepository.findByTitleContaining(search, pageable);                      // 카테고리 X

                case "content" -> hasCategory
                        ? communityRepository.findByCategoryAndContentContaining(category, search, pageable)    // 카테고리 O
                        : communityRepository.findByContentContaining(search, pageable);                        // 카테고리 X

                case "nickName" -> hasCategory
                        ? communityRepository.findByCategoryAndNickNameContaining(category, search, pageable)   // 카테고리 O
                        : communityRepository.findByNickNameContaining(search, pageable);                       // 카테고리 X

                default -> hasCategory
                        ? communityRepository.findByCategory(category, pageable)                                // 카테고리 O
                        : communityRepository.findAll(pageable);                                                // 카테고리 X
            };
        }
                return communityEntities.map(el -> CommunityDto.builder()
                .id(el.getId())
                .title(el.getTitle())
                .content(el.getContent())
                .nickName(el.getNickName())
                .count(el.getCount())
                .category(el.getCategory())
                .memberId(el.getMemberEntity().getId())
                .createTime(el.getCreateTime())
                .updateTime(el.getUpdateTime())
                .build());
    }



    // ======================================     게시글 상세     ==========================================//

    @Override
    @Transactional(readOnly = true)
    public CommunityDto communityDetail(Long id) {
        // 게시글 조회
        CommunityEntity communityEntity = communityRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("게시글(id)이 존재하지 않습니다."));

        return CommunityDto.builder()
                .id(communityEntity.getId())
                .title(communityEntity.getTitle())
                .content(communityEntity.getContent())
                .nickName(communityEntity.getNickName())
                .category(communityEntity.getCategory())
                .memberId(communityEntity.getMemberEntity().getId())
                .attachFile(communityEntity.getAttachFile())
                .newFileName(communityEntity.getCommunityFileEntities().get(0).getNewFileName())
                .oldFileName(communityEntity.getCommunityFileEntities().get(0).getOldFileName())
                .createTime(communityEntity.getCreateTime())
                .updateTime(communityEntity.getUpdateTime())
                .count(communityEntity.getCount())
                .build();
    }





    // ======================================     조회수 증가(쿼리문 활용)     ==========================================//

    @Override
    public void count(Long id) {
        communityRepository.updateCount(id);
    }
}
