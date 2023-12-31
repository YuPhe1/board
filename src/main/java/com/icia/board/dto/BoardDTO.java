package com.icia.board.dto;

import com.icia.board.entity.BoardEntity;
import com.icia.board.entity.BoardFileEntity;
import com.icia.board.entity.CommentEntity;
import com.icia.board.util.UtilClass;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    private List<MultipartFile> boardFile;
    private List<String> originalFileName = new ArrayList<>();
    private List<String> storedFileName = new ArrayList<>();
    private List<CommentDTO> commentDTOList = new ArrayList<>();
    public static BoardDTO toDTO(BoardEntity boardEntity){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setCreatedAt(UtilClass.dateTimeFormat(boardEntity.getCreatedAt()));

        // 파일 첨부 여부에 따라 파일이름 가져가기
        if(boardEntity.getFileAttached() == 1){
            boardDTO.setFileAttached(1);
            for(BoardFileEntity boardFileEntity : boardEntity.getBoardFileEntityList()){
                boardDTO.getOriginalFileName().add(boardFileEntity.getOriginalFileName());
                boardDTO.getStoredFileName().add(boardFileEntity.getStoredFileName());
            }
        } else {
            boardDTO.setFileAttached(0);
        }
        boardEntity.getCommentEntityList().forEach(commentEntity -> {
            CommentDTO commentDTO = CommentDTO.toDTO(commentEntity);
            boardDTO.getCommentDTOList().add(commentDTO);
        });
        return boardDTO;
    }
}
