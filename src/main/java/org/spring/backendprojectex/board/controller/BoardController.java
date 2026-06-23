package org.spring.backendprojectex.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.board.dto.BoardDto;
import org.spring.backendprojectex.board.service.BoardService;
import org.spring.backendprojectex.common.PagingUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Log4j2
public class BoardController {

    private final BoardService boardService;


    // ===========================     게시글 (전체)조회 검색,페이징    ==============================//
    @GetMapping({"","/boardList"})
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 5, sort="id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(name = "subject", required = false) String subject,
                            @RequestParam(name = "search", required = false) String search) {

        Page<BoardDto> boardList = boardService.boardList(pageable,subject,search);

        PagingUtil.setPaging(model,boardList,5);

        model.addAttribute("boardList",boardList);

        return "/board/boardList";
    }



    // ===========================     게시글 작성    ==============================//
    @GetMapping("/write")
    public String write2(BoardDto boardDto) {
        return "/board/boardWrite";
    }


    @PostMapping("/write")
    public String write2Ok(@Valid BoardDto boardDto, BindingResult bindingResult) throws IOException {
        if(bindingResult.hasErrors()){
            return "/board/boardWrite2";
        }
        // 파일 있음
        boardService.boardInsert(boardDto);
        return "redirect:/board/boardList";
    }

    // ===========================     게시글 수정    ==============================//

    @GetMapping("/update/{id}")
    public String update2Ok(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.boardDetail(id);
        model.addAttribute("board", boardDto);
        return "/board/boardUpdate";
    }

    @PostMapping("/update") // form
    public String update2(@ModelAttribute BoardDto boardDto, Model model) throws IOException {

        boardService.boardUpdate(boardDto);

        return "redirect:/board/detail/" + boardDto.getId();
    }



    // ===========================     게시글 삭제    ==============================//
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.boardDelete(id);
        return "redirect:/board/boardList";
    }
    // ===========================     게시글 (상세)조회   ==============================//
    @GetMapping("/detail/{id}")
    public String detail2(@PathVariable("id") Long id, Model model) {
        // hit
        boardService.updateHit(id);

        BoardDto boardDto = boardService.boardDetail(id);
        model.addAttribute("board", boardDto);
        return  "/board/boardDetail";
    }
}
