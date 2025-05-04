package br.com.devcoelho.taskboard.dto.mappers;

import br.com.devcoelho.taskboard.dto.CardDTO;
import br.com.devcoelho.taskboard.dto.CardSummaryDTO;
import br.com.devcoelho.taskboard.model.BoardColumn;
import br.com.devcoelho.taskboard.model.Card;
import br.com.devcoelho.taskboard.repository.BoardColumnRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardMapper {

  private final BlockMapper blockMapper;
  private final BoardColumnRepository boardColumnRepository;

  public CardDTO toDto(Card card) {
    if (card == null) {
      return null;
    }

    return CardDTO.builder()
        .id(card.getId())
        .title(card.getTitle())
        .description(card.getDescription())
        .createdAt(card.getCreatedAt())
        .updatedAt(card.getUpdatedAt())
        .boardColumnId(card.getBoardColumn() != null ? card.getBoardColumn().getId() : null)
        .boardColumnName(card.getBoardColumn() != null ? card.getBoardColumn().getName() : null)
        .blocks(blockMapper.toDtoList(card.getBlocks()))
        .blocked(card.isBlocked())
        .build();
  }

  public CardSummaryDTO toSummaryDto(Card card) {
    if (card == null) {
      return null;
    }

    return CardSummaryDTO.builder()
        .id(card.getId())
        .title(card.getTitle())
        .createdAt(card.getCreatedAt())
        .blocked(card.isBlocked())
        .columnName(card.getBoardColumn() != null ? card.getBoardColumn().getName() : null)
        .build();
  }

  public List<CardDTO> toDtoList(List<Card> cards) {
    if (cards == null) {
      return List.of();
    }

    return cards.stream().map(this::toDto).collect(Collectors.toList());
  }

  public List<CardSummaryDTO> toSummaryDtoList(List<Card> cards) {
    if (cards == null) {
      return List.of();
    }

    return cards.stream().map(this::toSummaryDto).collect(Collectors.toList());
  }

  public Card toEntity(CardDTO dto) {
    if (dto == null) {
      return null;
    }

    Card card =
        Card.builder()
            .id(dto.getId())
            .title(dto.getTitle())
            .description(dto.getDescription())
            .createdAt(dto.getCreatedAt())
            .updatedAt(dto.getUpdatedAt())
            .build();

    if (dto.getBoardColumnId() != null) {
      BoardColumn boardColumn = new BoardColumn();
      boardColumn.setId(dto.getBoardColumnId());
      card.setBoardColumn(boardColumn);
    }

    return card;
  }

  public Card updateEntityFromDto(CardDTO dto, Card entity) {
    if (dto == null) {
      return entity;
    }

    if (dto.getTitle() != null) {
      entity.setTitle(dto.getTitle());
    }

    if (dto.getDescription() != null) {
      entity.setDescription(dto.getDescription());
    }

    if (dto.getBoardColumnId() != null
        && (entity.getBoardColumn() == null
            || !dto.getBoardColumnId().equals(entity.getBoardColumn().getId()))) {

      BoardColumn boardColumn = boardColumnRepository.findById(dto.getBoardColumnId()).orElse(null);

      if (boardColumn != null) {
        entity.setBoardColumn(boardColumn);
      }
    }

    return entity;
  }
}
