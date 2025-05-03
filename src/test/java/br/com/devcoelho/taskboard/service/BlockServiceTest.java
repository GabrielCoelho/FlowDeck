package br.com.devcoelho.taskboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.devcoelho.taskboard.exception.CardAlreadyBlockedException;
import br.com.devcoelho.taskboard.exception.CardNotBlockedException;
import br.com.devcoelho.taskboard.exception.ResourceNotFoundException;
import br.com.devcoelho.taskboard.model.Block;
import br.com.devcoelho.taskboard.model.Card;
import br.com.devcoelho.taskboard.repository.BlockRepository;
import br.com.devcoelho.taskboard.repository.CardRepository;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BlockServiceTest {

    @Mock
    private BlockRepository blockRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private BlockService blockService;

    private Card testCard;
    private Block testBlock;
    private Block resolvedBlock;

    @BeforeEach
    void setUp() {
        // Configuração do card de teste
        testCard = new Card();
        testCard.setId(1L);
        testCard.setTitle("Test Card");

        // Configuração do bloco ativo (não resolvido)
        testBlock = new Block();
        testBlock.setId(1L);
        testBlock.setCard(testCard);
        testBlock.setBlockedAt(OffsetDateTime.now().minusDays(1));
        testBlock.setBlockReason("Test block reason");

        // Configuração de um bloco resolvido
        resolvedBlock = new Block();
        resolvedBlock.setId(2L);
        resolvedBlock.setCard(testCard);
        resolvedBlock.setBlockedAt(OffsetDateTime.now().minusDays(2));
        resolvedBlock.setBlockReason("Old block reason");
        resolvedBlock.setUnblockedAt(OffsetDateTime.now().minusDays(1));
        resolvedBlock.setUnblockReason("Resolved");
    }

    @Test
    @DisplayName("Deve encontrar bloqueios por ID do card")
    void shouldFindBlocksByCardId() {
        // Arrange
        List<Block> expectedBlocks = Arrays.asList(testBlock, resolvedBlock);
        when(blockRepository.findByCardId(1L)).thenReturn(expectedBlocks);

        // Act
        List<Block> result = blockService.findByCardId(1L);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(testBlock));
        assertTrue(result.contains(resolvedBlock));

        // Verify
        verify(blockRepository).findByCardId(1L);
    }

    @Test
    @DisplayName("Deve encontrar um bloqueio pelo ID")
    void shouldFindBlockById() {
        // Arrange
        when(blockRepository.findById(1L)).thenReturn(Optional.of(testBlock));

        // Act
        Block result = blockService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test block reason", result.getBlockReason());

        // Verify
        verify(blockRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o bloqueio não for encontrado")
    void shouldThrowExceptionWhenBlockNotFound() {
        // Arrange
        when(blockRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> blockService.findById(99L)
        );

        // Verify
        assertEquals("Blocked not found with ID: 99", exception.getMessage());
        verify(blockRepository).findById(99L);
    }

    @Test
    @DisplayName("Deve verificar corretamente se um card está bloqueado")
    void shouldCheckIfCardIsBlocked() {
        // Arrange - Card com bloqueio ativo
        when(blockRepository.findActiveBlockByCardId(1L)).thenReturn(Optional.of(testBlock));

        // Act & Assert
        assertTrue(blockService.isCardBlocked(1L));

        // Arrange - Card sem bloqueio ativo
        when(blockRepository.findActiveBlockByCardId(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertFalse(blockService.isCardBlocked(2L));

        // Verify
        verify(blockRepository).findActiveBlockByCardId(1L);
        verify(blockRepository).findActiveBlockByCardId(2L);
    }

    @Test
    @DisplayName("Deve bloquear um card com sucesso")
    void shouldBlockCardSuccessfully() {
        // Arrange
        when(blockRepository.findActiveBlockByCardId(1L)).thenReturn(Optional.empty());
        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));
        when(blockRepository.save(any(Block.class))).thenReturn(testBlock);

        // Act
        Block result = blockService.blockCard(1L, "Test block reason");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test block reason", result.getBlockReason());

        // Verify
        verify(blockRepository).findActiveBlockByCardId(1L);
        verify(cardRepository).findById(1L);

        // Capturar o objeto Block enviado para save() e verificar os valores
        ArgumentCaptor<Block> blockCaptor = ArgumentCaptor.forClass(Block.class);
        verify(blockRepository).save(blockCaptor.capture());

        Block capturedBlock = blockCaptor.getValue();
        assertEquals(testCard, capturedBlock.getCard());
        assertEquals("Test block reason", capturedBlock.getBlockReason());
        assertNotNull(capturedBlock.getBlockedAt());
        assertNull(capturedBlock.getUnblockedAt());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar bloquear um card já bloqueado")
    void shouldThrowExceptionWhenBlockingAlreadyBlockedCard() {
        // Arrange
        when(blockRepository.findActiveBlockByCardId(1L)).thenReturn(Optional.of(testBlock));

        // Act & Assert
        CardAlreadyBlockedException exception = assertThrows(
            CardAlreadyBlockedException.class,
            () -> blockService.blockCard(1L, "New block reason")
        );

        // Verify
        assertEquals("The card (ID: 1) is already blocked. Operation can't be done", exception.getMessage());
        verify(blockRepository).findActiveBlockByCardId(1L);
        verify(cardRepository, never()).findById(any());
        verify(blockRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar bloquear um card inexistente")
    void shouldThrowExceptionWhenBlockingNonExistentCard() {
        // Arrange
        when(blockRepository.findActiveBlockByCardId(99L)).thenReturn(Optional.empty());
        when(cardRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> blockService.blockCard(99L, "Block reason")
        );

        // Verify
        assertEquals("Card not found with ID: 99", exception.getMessage());
        verify(blockRepository).findActiveBlockByCardId(99L);
        verify(cardRepository).findById(99L);
        verify(blockRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve desbloquear um card com sucesso")
    void shouldUnblockCardSuccessfully() {
        // Arrange
        when(blockRepository.findActiveBlockByCardId(1L)).thenReturn(Optional.of(testBlock));
        when(blockRepository.save(any(Block.class))).thenReturn(testBlock);

        // Act
        Block result = blockService.unblockCard(1L, "Unblock reason");

        // Assert
        assertNotNull(result);

        // Capturar o objeto Block enviado para save()
        ArgumentCaptor<Block> blockCaptor = ArgumentCaptor.forClass(Block.class);
        verify(blockRepository).save(blockCaptor.capture());

        Block capturedBlock = blockCaptor.getValue();
        assertEquals("Unblock reason", capturedBlock.getUnblockReason());
        assertNotNull(capturedBlock.getUnblockedAt());

        // Verify
        verify(blockRepository).findActiveBlockByCardId(1L);
        verify(blockRepository).save(any(Block.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar desbloquear um card não bloqueado")
    void shouldThrowExceptionWhenUnblockingNonBlockedCard() {
        // Arrange
        when(blockRepository.findActiveBlockByCardId(1L)).thenReturn(Optional.empty());

        // Act & Assert
        CardNotBlockedException exception = assertThrows(
            CardNotBlockedException.class,
            () -> blockService.unblockCard(1L, "Unblock reason")
        );

        // Verify
        assertEquals("The card (ID: 1) isn't blocked. Operation can't be done", exception.getMessage());
        verify(blockRepository).findActiveBlockByCardId(1L);
        verify(blockRepository, never()).save(any());
    }
}
