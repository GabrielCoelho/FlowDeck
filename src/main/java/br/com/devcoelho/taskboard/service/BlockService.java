package br.com.devcoelho.taskboard.service;

import br.com.devcoelho.taskboard.exception.CardAlreadyBlockedException;
import br.com.devcoelho.taskboard.exception.CardNotBlockedException;
import br.com.devcoelho.taskboard.exception.ResourceNotFoundException;
import br.com.devcoelho.taskboard.model.Block;
import br.com.devcoelho.taskboard.model.Card;
import br.com.devcoelho.taskboard.repository.BlockRepository;
import br.com.devcoelho.taskboard.repository.CardRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlockService {

  private final BlockRepository blockRepository;
  private final CardRepository cardRepository;

  /** Busca todos os bloqueios de um card */
  public List<Block> findByCardId(Long cardId) {
    return blockRepository.findByCardId(cardId);
  }

  /** Busca um bloqueio pelo ID */
  public Block findById(Long id) {
    return blockRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Block not found with id: ", id));
  }

  /** Verifica se um card está bloqueado */
  public boolean isCardBlocked(Long cardId) {
    return blockRepository.findActiveBlockByCardId(cardId).isPresent();
  }

  /** Bloqueia um card */
  @Transactional
  public Block blockCard(Long cardId, String reason) {
    // Verifica se o card já está bloqueado
    if (isCardBlocked(cardId)) {
      throw new CardAlreadyBlockedException(cardId);
    }

    // Busca o card
    Card card =
        cardRepository
            .findById(cardId)
            .orElseThrow(() -> new ResourceNotFoundException("Card", cardId));

    // Cria o bloqueio
    Block block =
        Block.builder().card(card).blockedAt(OffsetDateTime.now()).blockReason(reason).build();

    return blockRepository.save(block);
  }

  /** Desbloqueia um card */
  @Transactional
  public Block unblockCard(Long cardId, String reason) {
    // Busca o bloqueio ativo
    Block block =
        blockRepository
            .findActiveBlockByCardId(cardId)
            .orElseThrow(() -> new CardNotBlockedException(cardId));

    // Atualiza o bloqueio
    block.setUnblockedAt(OffsetDateTime.now());
    block.setUnblockReason(reason);

    return blockRepository.save(block);
  }
}
