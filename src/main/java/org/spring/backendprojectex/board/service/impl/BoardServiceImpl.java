package org.spring.backendprojectex.board.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.board.dto.BoardDto;
import org.spring.backendprojectex.board.entity.BoardEntity;
import org.spring.backendprojectex.board.entity.BoardFileEntity;
import org.spring.backendprojectex.board.repository.BoardFileRepository;
import org.spring.backendprojectex.board.repository.BoardRepository;
import org.spring.backendprojectex.board.service.BoardService;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;


    // ======================   게시글 (전체)조회 (페이징,검색)   ======================//
    @Override
    public void boardInsert(BoardDto boardDto) throws IOException{
        // 1. 회원(id) 체크
        MemberEntity memberEntity = memberRepository.findById(boardDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원(id)이 없습니다."));

        // 2. 파일 첨부 여부 (삼항연산자 처리)
        boolean hasFile = boardDto.getBoardFile() != null && !boardDto.getBoardFile().isEmpty();
        int attachFile = hasFile ? 1 : 0;

        // 3. BoardEntity를 빌더 패턴으로 생성 (정적 변환 메서드 대신 직접 매핑)
        BoardEntity boardEntity = BoardEntity.builder()
                .boardTitle(boardDto.getBoardTitle())     // DTO 필드명에 맞게 대조 확인 필요
                .boardContent(boardDto.getBoardContent()) // DTO 필드명에 맞게 대조 확인 필요
                .nickName(boardDto.getNickName())  // DTO 필드명에 맞게 대조 확인 필요
                .memberEntity(memberEntity)               // 1번에서 검증 완료된 영속성 메모리의 memberEntity 세팅
                .attachFile(attachFile)                   // 파일 첨부 여부 설정 (1 또는 0)
                .hit(0)
                .build();

        // 4. 게시글 정보 DB 저장
        BoardEntity savedBoard = boardRepository.save(boardEntity);

        // 5. 첨부 파일이 존재하는 경우 로컬 파일 저장 및 파일 엔티티 등록 처리
        if (hasFile) {
            MultipartFile boardFile = boardDto.getBoardFile();

            // 원본 파일명 추출 및 UUID를 활용한 암호화 파일명 생성
            String oldFileName = boardFile.getOriginalFilename();
            String newFileName = UUID.randomUUID() + "_" + oldFileName;
            String fileSavePath = "E:/full/upload/backend/board/" + newFileName;

            boardFile.transferTo(new File(fileSavePath));
            //  파일 저장
            BoardFileEntity boardFileEntity = BoardFileEntity.builder()
                    .oldFileName(oldFileName)
                    .newFileName(newFileName)
                    .boardEntity(savedBoard) // 4번에서 정상 저장되어 Auto_Increment ID가 부여된 board 객체 세팅
                    .build();

            // 파일 세부 정보 DB 저장 실행
            boardFileRepository.save(boardFileEntity);
        }



    }

    // ======================   게시글 (상세)조회   ======================//

    @Override
    public BoardDto boardDetail(Long id) {
        // 1. 게시글 엔티티 조회
        BoardEntity boardEntity = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 2. 기본 정보 DTO 빌드
        BoardDto.BoardDtoBuilder builder = BoardDto.builder()
                .id(boardEntity.getId())
                .boardTitle(boardEntity.getBoardTitle())
                .boardContent(boardEntity.getBoardContent())
                .nickName(boardEntity.getNickName())
                .hit(boardEntity.getHit())
                .attachFile(boardEntity.getAttachFile())
                .createTime(boardEntity.getCreateTime())
                .updateTime(boardEntity.getUpdateTime());

        // 3. 파일이 첨부된 게시글일 경우 파일 정보 추가 세팅
        if (boardEntity.getAttachFile() == 1) {
            Optional<BoardFileEntity> fileEntity = boardFileRepository.findByBoardEntity_Id(id);
            if(fileEntity.isPresent()){
                builder.oldFileName(fileEntity.get().getOldFileName())
                        .newFileName(fileEntity.get().getNewFileName());
            }
        }

        return builder.build();
    }
    // ======================   게시글 삭제   ======================//

    @Override
    public void boardDelete(Long id) {
        BoardEntity boardEntity = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 파일이 있다면 파일 데이터 먼저 삭제 (외래키 제약조건 방지)
        if (boardEntity.getAttachFile() == 1) {
            boardFileRepository.deleteByBoardEntity_Id(id);
        }
        // DB삭제
        boardRepository.delete(boardEntity);

    }
    // ======================   게시글 수정  ======================//

    @Override
    public void boardUpdate(BoardDto boardDto) throws IOException {
        // 게시글 id체크
        BoardEntity boardEntity = boardRepository.findById(boardDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 1. 기본 정보 담기
        boardEntity.setBoardTitle(boardDto.getBoardTitle());
        boardEntity.setBoardContent(boardDto.getBoardContent());

        // 2. 새로운 파일이 업로드 되었는지 확인
        boolean hasNewFile = boardDto.getBoardFile() != null && !boardDto.getBoardFile().isEmpty();

        if (hasNewFile) {
            MultipartFile boardFile = boardDto.getBoardFile();

            // 기존 파일이 있었다면 DB에서 기존 파일 정보 삭제
            if (boardEntity.getAttachFile() == 1) {
                boardFileRepository.deleteByBoardEntity_Id(boardEntity.getId());
            }

            // 새 파일 로컬에 저장
            String oldFileName = boardFile.getOriginalFilename();
            String newFileName = UUID.randomUUID() + "_" + oldFileName;
            String fileSavePath = "E:/full/upload/backend/board/" + newFileName;
            boardFile.transferTo(new File(fileSavePath));

            // 새 파일 정보 DB에 저장
            BoardFileEntity boardFileEntity = BoardFileEntity.builder()
                    .oldFileName(oldFileName)
                    .newFileName(newFileName)
                    .boardEntity(boardEntity)
                    .build();
            boardFileRepository.save(boardFileEntity);

            // 첨부파일 있음(1) 상태로 업데이트
            boardEntity.setAttachFile(1);
        }

    }
    // ======================   게시글 조회수   ======================//

    @Override
    public void updateHit(Long id) {
        BoardEntity boardEntity = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 조회수 1 증가 (트랜잭션 종료 시 자동 반영됨)
        boardEntity.setHit(boardEntity.getHit() + 1);

    }

    // ======================   게시글 (전체)조회 (페이징,검색)   ======================//
    @Override
    public Page<BoardDto> boardList(Pageable pageable, String subject, String search) {
        Page<BoardEntity> boardEntities;

        // 검색조건 처리
        if ( subject==null || search == null || search.isBlank()) {
            boardEntities = boardRepository.findAll(pageable);
        } else {
            boardEntities = switch (subject) {
                case "boardTitle" -> boardRepository.findByBoardTitleContaining(search, pageable);
                case "boardContent" -> boardRepository.findByBoardContentContaining(search, pageable);
                case "nickName" -> boardRepository.findByNickNameContaining(search, pageable);
                default -> boardRepository.findAll(pageable);
            };
        }

        // 엔티티를 DTO로 변환하여 반환
        return boardEntities.map(board -> BoardDto.builder()
                .id(board.getId())
                .boardTitle(board.getBoardTitle())
                .nickName(board.getNickName())
                .hit(board.getHit())
                .attachFile(board.getAttachFile())
                .createTime(board.getCreateTime())
                .build());
    }
}
