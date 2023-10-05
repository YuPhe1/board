package com.icia.board.controller;

import com.icia.board.dto.BoardDTO;
import com.icia.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/save")
    public String savePage() {
        return "boardPages/boardSave";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) {
        boardService.save(boardDTO);
        return "redirect:/board";
    }

    /*
        rest api
     */
    @GetMapping
    public String findAll(Model model,
                          @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        Page<BoardDTO> boardDTOList = boardService.findAll(page);
        model.addAttribute("boardList", boardDTOList);
        // 목록 하단에 보여줄 페이지 번호
        int blockLimit = 3;
        int startPage = (((int) (Math.ceil((double) page / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boardDTOList.getTotalPages()) ? startPage + blockLimit - 1 : boardDTOList.getTotalPages();
//        if ((startPage + blockLimit - 1) < boardDTOList.getTotalPages()) {
//            endPage = startPage + blockLimit - 1;
//        } else {
//            endPage = boardDTOS.getTotalPages();
//        }
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardPages/boardList";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        try {
            boardService.increaseHits(id);
            BoardDTO boardDTO = boardService.findById(id);
            model.addAttribute("board", boardDTO);
            return "boardPages/boardDetail";
        } catch (NoSuchElementException e) {
            return "boardPages/notFound";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.delete(id);
        return "redirect:/board";
    }

    @GetMapping("/update/{id}")
    public String updatePage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        return "boardPages/boardUpdate";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO) {
        boardService.update(boardDTO);
        return "redirect:/board/" + boardDTO.getId();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity axiosDelete(@PathVariable("id") Long id) {
        boardService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody BoardDTO boardDTO) {
        boardService.update(boardDTO);
        return new ResponseEntity(HttpStatus.OK);
    }
}
