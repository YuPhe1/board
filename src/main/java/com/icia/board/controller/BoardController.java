package com.icia.board.controller;

import com.icia.board.dto.BoardDTO;
import com.icia.board.service.BoardService;
import lombok.RequiredArgsConstructor;
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
            boardService.increaseHits(id);
            BoardDTO boardDTO = boardService.findById(id);
            model.addAttribute("board", boardDTO);
            return "boardPages/boardDetail";
        } catch (NoSuchElementException e){
            return "boardPages/notFound";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        boardService.delete(id);
        return "redirect:/board";
    }

    @GetMapping("/update/{id}")
    public String updatePage(@PathVariable("id") Long id, Model model){
        model.addAttribute("board", boardService.findById(id));
        return "boardPages/boardUpdate";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO){
        boardService.update(boardDTO);
        return "redirect:/board/" + boardDTO.getId();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id, @RequestParam("boardPass") String boardPass){
        BoardDTO boardDTO = boardService.findById(id);
        if(boardDTO.getBoardPass().equals(boardPass)) {
            boardService.delete(id);
            return new ResponseEntity(HttpStatus.OK);
        }
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody BoardDTO boardDTO){
        boardService.update(boardDTO);
        return new ResponseEntity(HttpStatus.OK);
    }
}
