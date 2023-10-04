package com.icia.board;

import com.icia.board.dto.BoardDTO;
import com.icia.board.repository.BoardRepository;
import com.icia.board.service.BoardService;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class BoardTest {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;

    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("글작성")
    public void boardSaveTest(){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardWriter("test_writer");
        boardDTO.setBoardPass("test_pass");
        boardDTO.setBoardTitle("test_title");
        boardDTO.setBoardContents("test_contents");
        Long saveId = boardService.save(boardDTO);
        BoardDTO findBoard = boardService.findById(saveId);
        assertThat(boardDTO.getBoardTitle()).isEqualTo(findBoard.getBoardTitle());
    }
}
