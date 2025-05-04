package br.com.devcoelho.taskboard.dto.mappers;

import br.com.devcoelho.taskboard.dto.BoardDTO;
import br.com.devcoelho.taskboard.dto.BoardSummaryDTO;
import br.com.devcoelho.taskboard.model.Board;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardMapper {

  private final BoardColumnMapper boardColumnMapper;

  public BoardDTO toDto(Board board) {
    if (board == null) {
      return null;
    }

    BoardDTO dto =
        BoardDTO.builder()
            .id(board.getId())
            .name(board.getName())
            .columns(boardColumnMapper.toDtoList(board.getColumns()))
            .build();

    // Calcular o total de cards
    int totalCards =
        board.getColumns() != null
            ? board.getColumns().stream()
                .mapToInt(column -> column.getCards() != null ? column.getCards().size() : 0)
                .sum()
            : 0;

    dto.setTotalCards(totalCards);

    return dto;
  }

  public BoardSummaryDTO toSummaryDto(Board board) {
    if (board == null) {
      return null;
    }

    int columnCount = board.getColumns() != null ? board.getColumns().size() : 0;

    int cardCount =
        board.getColumns() != null
            ? board.getColumns().stream()
                .mapToInt(column -> column.getCards() != null ? column.getCards().size() : 0)
                .sum()
            : 0;

    return BoardSummaryDTO.builder()
        .id(board.getId())
        .name(board.getName())
        .columnCount(columnCount)
        .cardCount(cardCount)
        .build();
  }

  public List<BoardDTO> toDtoList(List<Board> boards) {
    if (boards == null) {
      return List.of();
    }

    return boards.stream().map(this::toDto).collect(Collectors.toList());
  }

  public List<BoardSummaryDTO> toSummaryDtoList(List<Board> boards) {
    if (boards == null) {
      return List.of();
    }

    return boards.stream().map(this::toSummaryDto).collect(Collectors.toList());
  }

  public Board toEntity(BoardDTO dto) {
    if (dto == null) {
      return null;
    }

    return Board.builder().id(dto.getId()).name(dto.getName()).build();
  }

  public Board updateEntityFromDto(BoardDTO dto, Board entity) {
    if (dto == null) {
      return entity;
    }

    if (dto.getName() != null) {
      entity.setName(dto.getName());
    }

    return entity;
  }
}
