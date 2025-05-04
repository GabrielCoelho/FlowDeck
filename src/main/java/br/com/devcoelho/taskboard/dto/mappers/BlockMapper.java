package br.com.devcoelho.taskboard.dto.mappers;

import br.com.devcoelho.taskboard.dto.BlockDTO;
import br.com.devcoelho.taskboard.model.Block;
import br.com.devcoelho.taskboard.model.Card;
import br.com.devcoelho.taskboard.repository.CardRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlockMapper {

  private final CardRepository cardRepository;

  public BlockDTO toDto(Block block) {
    if (block == null) {
      return null;
    }

    return BlockDTO.builder()
        .id(block.getId())
        .blockedAt(block.getBlockedAt())
        .blockReason(block.getBlockReason())
        .unblockedAt(block.getUnblockedAt())
        .unblockReason(block.getUnblockReason())
        .cardId(block.getCard() != null ? block.getCard().getId() : null)
        .build();
  }

  public List<BlockDTO> toDtoList(List<Block> blocks) {
    if (blocks == null) {
      return List.of();
    }

    return blocks.stream().map(this::toDto).collect(Collectors.toList());
  }

  public Block toEntity(BlockDTO dto) {
    if (dto == null) {
      return null;
    }

    Block block =
        Block.builder()
            .id(dto.getId())
            .blockedAt(dto.getBlockedAt())
            .blockReason(dto.getBlockReason())
            .unblockedAt(dto.getUnblockedAt())
            .unblockReason(dto.getUnblockReason())
            .build();

    if (dto.getCardId() != null) {
      Card card = new Card();
      card.setId(dto.getCardId());
      block.setCard(card);
    }

    return block;
  }

  public Block updateEntityFromDto(BlockDTO dto, Block entity) {
    if (dto == null) {
      return entity;
    }

    if (dto.getBlockReason() != null) {
      entity.setBlockReason(dto.getBlockReason());
    }

    if (dto.getUnblockReason() != null) {
      entity.setUnblockReason(dto.getUnblockReason());
    }

    if (dto.getUnblockedAt() != null) {
      entity.setUnblockedAt(dto.getUnblockedAt());
    }

    return entity;
  }
}
