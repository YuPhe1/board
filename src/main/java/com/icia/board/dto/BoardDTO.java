package com.icia.board.dto;

import com.icia.board.entity.BoardEntity;
import com.icia.board.util.UtilClass;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardDTO {

    private Long id;
    private String boardWriter;
    private String boardTitle;
    private String boardPass;
    private String boardContents;
    private String createdAt;
    private int boardHits;
    private int fileAttached;
    private MultipartFile boardFile;
    private String originalFileName;
    private String storedFileName;
    public static BoardDTO toDTO(BoardEntity boardEntity){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setCreatedAt(UtilClass.dateTimeFormat(boardEntity.getCreatedAt()));

//        BoardDTO boardDTO = BoardDTO.builder()
//                .id(boardEntity.getId())
//                .boardWriter(boardEntity.getBoardWriter())
//                .boardTitle(boardEntity.getBoardTitle())
//                .boardPass(boardEntity.getBoardPass())
//                .boardContents(boardEntity.getBoardContents())
//                .boardHits(boardEntity.getBoardHits())
//                .createdAt(UtilClass.dateTimeFormat((boardEntity.getCreatedAt())))
//                .build();
        return boardDTO;
    }
}
