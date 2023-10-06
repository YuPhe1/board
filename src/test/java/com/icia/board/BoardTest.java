package com.icia.board;

import com.icia.board.dto.BoardDTO;
import com.icia.board.entity.BoardEntity;
import com.icia.board.entity.BoardFileEntity;
import com.icia.board.repository.BoardRepository;
import com.icia.board.service.BoardService;
import static org.assertj.core.api.Assertions.*;

import com.icia.board.util.UtilClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardTest {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;

    private BoardDTO newBoardDTO(int i){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardWriter("test_writer" + i);
        boardDTO.setBoardPass("test_pass" + i);
        boardDTO.setBoardTitle("test_title" + i);
        boardDTO.setBoardContents("test_contents" + i);
        return boardDTO;
    }

    @Test
    @DisplayName("글 목록 넣기")
    public void dataInsert(){
        IntStream.rangeClosed(1, 50).forEach( i -> {
            try {
                boardService.save(newBoardDTO(i));
            } catch (IOException e){

            }
        });
    }

    @Test
    @DisplayName("페이지 객체 확인")
    public void pagingMethod(){
        int page = 1;
        int pageLimit = 5;
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫페이지인지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막페이지인지 여부

        // Page<BoardEntity> -> Page<BoardDTO>
        Page<BoardDTO> boardList = boardEntities.map(boardEntity ->
                BoardDTO.builder()
                        .id(boardEntity.getId())
                        .boardWriter(boardEntity.getBoardWriter())
                        .boardTitle(boardEntity.getBoardTitle())
                        .boardHits(boardEntity.getBoardHits())
                        .createdAt(UtilClass.dateTimeFormat((boardEntity.getCreatedAt())))
                        .build());

        System.out.println("boardList.getContent() = " + boardList.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardList.getTotalElements() = " + boardList.getTotalElements()); // 전체 글갯수
        System.out.println("boardList.getNumber() = " + boardList.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardList.getTotalPages() = " + boardList.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardList.getSize() = " + boardList.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardList.hasPrevious() = " + boardList.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardList.isFirst() = " + boardList.isFirst()); // 첫페이지인지 여부
        System.out.println("boardList.isLast() = " + boardList.isLast()); // 마지막페이지인지 여부
    }
    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("글작성")
    public void boardSaveTest() throws IOException {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardWriter("test_writer");
        boardDTO.setBoardPass("test_pass");
        boardDTO.setBoardTitle("test_title");
        boardDTO.setBoardContents("test_contents");
        Long saveId = boardService.save(boardDTO);
        BoardDTO findBoard = boardService.findById(saveId);
        assertThat(boardDTO.getBoardTitle()).isEqualTo(findBoard.getBoardTitle());
    }

    @Test
    @DisplayName("검색 메서드 확인")
    public void searchMethod(){
//        List<BoardEntity> boardEntityList = boardRepository.findByBoardTitleContainingOrderById("1");
//        boardEntityList.forEach(boardEntity -> {
//            System.out.println(BoardDTO.toDTO(boardEntity));
//        });

        // 제목에 1이 포홤된 결과 페이징
        int page = 0;
        int pageLimit = 5;
        Page<BoardEntity> boardEntities = boardRepository.findByBoardTitleContaining("1", PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        String q = "1";
        boardEntities = boardRepository.findByBoardTitleContainingOrBoardWriterContaining(q, q, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        Page<BoardDTO> boardList = boardEntities.map(boardEntity ->
                BoardDTO.builder()
                        .id(boardEntity.getId())
                        .boardWriter(boardEntity.getBoardWriter())
                        .boardTitle(boardEntity.getBoardTitle())
                        .boardHits(boardEntity.getBoardHits())
                        .createdAt(UtilClass.dateTimeFormat((boardEntity.getCreatedAt())))
                        .build());

        System.out.println("boardList.getContent() = " + boardList.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardList.getTotalElements() = " + boardList.getTotalElements()); // 전체 글갯수
        System.out.println("boardList.getNumber() = " + boardList.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardList.getTotalPages() = " + boardList.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardList.getSize() = " + boardList.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardList.hasPrevious() = " + boardList.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardList.isFirst() = " + boardList.isFirst()); // 첫페이지인지 여부
        System.out.println("boardList.isLast() = " + boardList.isLast()); // 마지막페이지인지 여부
    }

    @Test
    @Transactional
    @DisplayName("참조관계 확인")
    public void findTest() {
        // BoardEntity조회
        Optional<BoardEntity> boardEntityOptional = boardRepository.findById(52L);
        BoardEntity boardEntity = boardEntityOptional.get();
        // boardEntitiy에서 BoardFileEntity 조회
        List<BoardFileEntity> boardFileEntityList = boardEntity.getBoardFileEntityList();
        boardFileEntityList.forEach(boardFileEntity -> {
            System.out.println(boardFileEntity.getOriginalFileName());
            System.out.println(boardFileEntity.getStoredFileName());
        });
    }
}
