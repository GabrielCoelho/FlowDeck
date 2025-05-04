package br.com.devcoelho.taskboard.dto.mappers;

import br.com.devcoelho.taskboard.dto.BoardColumnDTO;
import br.com.devcoelho.taskboard.model.Board;
import br.com.devcoelho.taskboard.model.BoardColumn;
import br.com.devcoelho.taskboard.repository.BoardRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardColumnMapper {

  private final CardMapper cardMapper;
  private final BoardRepository boardRepository;

  public BoardColumnDTO toDto(BoardColumn boardColumn) {
    if (boardColumn == null) {
      return null;
    }

    BoardColumnDTO dto =
        BoardColumnDTO.builder()
            .id(boardColumn.getId())
            .name(boardColumn.getName())
            .order(boardColumn.getOrder())
            .kind(boardColumn.getKind())
            .boardId(boardColumn.getBoard() != null ? boardColumn.getBoard().getId() : null)
            .cards(cardMapper.toDtoList(boardColumn.getCards()))
            .build();

    // Adicionar campo calculado
    dto.setCardCount(boardColumn.getCards() != null ? boardColumn.getCards().size() : 0);

    return dto;
  }

  public BoardColumnDTO toDtoWithoutCards(BoardColumn boardColumn) {
    if (boardColumn == null) {
      return null;
    }

    BoardColumnDTO dto =
        BoardColumnDTO.builder()
            .id(boardColumn.getId())
            .name(boardColumn.getName())
            .order(boardColumn.getOrder())
            .kind(boardColumn.getKind())
            .boardId(boardColumn.getBoard() != null ? boardColumn.getBoard().getId() : null)
            .build();

    // Adicionar campo calculado
    dto.setCardCount(boardColumn.getCards() != null ? boardColumn.getCards().size() : 0);

    return dto;
  }

  public List<BoardColumnDTO> toDtoList(List<BoardColumn> boardColumns) {
    if (boardColumns == null) {
      return List.of();
    }

    return boardColumns.stream().map(this::toDto).collect(Collectors.toList());
  }

  public List<BoardColumnDTO> toDtoListWithoutCards(List<BoardColumn> boardColumns) {
    if (boardColumns == null) {
      return List.of();
    }

    return boardColumns.stream().map(this::toDtoWithoutCards).collect(Collectors.toList());
  }

  public BoardColumn toEntity(BoardColumnDTO dto) {
    if (dto == null) {
      return null;
    }

    BoardColumn boardColumn =
        BoardColumn.builder()
            .id(dto.getId())
            .name(dto.getName())
            .order(dto.getOrder())
            .kind(dto.getKind())
            .build();

    if (dto.getBoardId() != null) {
      Board board = new Board();
      board.setId(dto.getBoardId());
      boardColumn.setBoard(board);
    }

    return boardColumn;
  }

  public BoardColumn updateEntityFromDto(BoardColumnDTO dto, BoardColumn entity) {
    if (dto == null) {
      return entity;
    }

    if (dto.getName() != null) {
      entity.setName(dto.getName());
    }

    if (dto.getOrder() > 0) {
      entity.setOrder(dto.getOrder());
    }

    if (dto.getKind() != null) {
      entity.setKind(dto.getKind());
    }

    if (dto.getBoardId() != null
        && (entity.getBoard() == null || !dto.getBoardId().equals(entity.getBoard().getId()))) {

      Board board = boardRepository.findById(dto.getBoardId()).orElse(null);

      if (board != null) {
        entity.setBoard(board);
      }
    }

    return entity;
  }
}
