package com.icia.board.controller;

import com.icia.board.dto.BoardDTO;
import com.icia.board.service.BoardService;
import lombok.RequiredArgsConstructor;
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
    public String savePage(){
        return "boardPages/boardSave";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO){
        boardService.save(boardDTO);
        return "redirect:/board";
    }

    @GetMapping
    public String findAll(Model model){
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "boardPages/boardList";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        try {
            BoardDTO boardDTO = boardService.findById(id);
            boardDTO.setBoardHits(boardDTO.getBoardHits() + 1);
            boardService.update(boardDTO);
            model.addAttribute("board", boardDTO);
            return "boardPages/boardDetail";
        } catch (NoSuchElementException e){
            return "boardPages/notFound";
        }

    }
}
