package br.com.devcoelho.taskboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import br.com.devcoelho.taskboard.exception.BlockedCardException;
import br.com.devcoelho.taskboard.exception.ResourceNotFoundException;
import br.com.devcoelho.taskboard.model.Block;
import br.com.devcoelho.taskboard.model.Board;
import br.com.devcoelho.taskboard.model.BoardColumn;
import br.com.devcoelho.taskboard.model.BoardColumnKind;
import br.com.devcoelho.taskboard.model.Card;
import br.com.devcoelho.taskboard.repository.BoardColumnRepository;
import br.com.devcoelho.taskboard.repository.CardRepository;

import java.time.OffsetDateTime;
import java.util.ArrayList;
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
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private BoardColumnRepository boardColumnRepository;

    @Mock
    private BlockService blockService;

    @InjectMocks
    private CardService cardService;

    private Card testCard;
    private Card blockedCard;
    private Block activeBlock;

    @BeforeEach
    void setUp() {
        // Configuração do card de teste
        testCard = new Card();
        testCard.setId(1L);
        testCard.setTitle("Test Card");
        testCard.setDescription("Test Description");
        testCard.setBoardColumn(new BoardColumn());
        testCard.setCreatedAt(OffsetDateTime.now().minusDays(1));

        // Configuração do card bloqueado
        blockedCard = new Card();
        blockedCard.setId(2L);
        blockedCard.setTitle("Blocked Card");
        blockedCard.setDescription("Blocked Card Description");
        blockedCard.setBoardColumn(new BoardColumn());
        blockedCard.setCreatedAt(OffsetDateTime.now().minusDays(2));

        // Bloco ativo
        activeBlock = new Block();
        activeBlock.setId(1L);
        activeBlock.setCard(blockedCard);
        activeBlock.setBlockedAt(OffsetDateTime.now().minusHours(5));
        activeBlock.setBlockReason("Test block reason");

        // Adicionando o bloco ao card bloqueado
        List<Block> blocks = new ArrayList<>();
        blocks.add(activeBlock);
        blockedCard.setBlocks(blocks);
    }

    @Test
    @DisplayName("Deve encontrar cards por ID da coluna")
    void shouldFindCardsByBoardColumnId() {
        // Arrange
        List<Card> expectedCards = Arrays.asList(testCard, blockedCard);
        when(cardRepository.findByBoardColumnId(1L)).thenReturn(expectedCards);

        // Act
        List<Card> result = cardService.findByBoardColumnId(1L);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(testCard));
        assertTrue(result.contains(blockedCard));

        // Verify
        verify(cardRepository).findByBoardColumnId(1L);
    }

    @Test
    @DisplayName("Deve encontrar cards por ID do board")
    void shouldFindCardsByBoardId() {
        // Arrange
        List<Card> expectedCards = Arrays.asList(testCard, blockedCard);
        when(cardRepository.findByBoardId(1L)).thenReturn(expectedCards);

        // Act
        List<Card> result = cardService.findByBoardId(1L);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(testCard));
        assertTrue(result.contains(blockedCard));

        // Verify
        verify(cardRepository).findByBoardId(1L);
    }

    @Test
    @DisplayName("Deve encontrar um card pelo ID")
    void shouldFindCardById() {
        // Arrange
        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));

        // Act
        Card result = cardService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Card", result.getTitle());
        assertEquals("Test Description", result.getDescription());

        // Verify
        verify(cardRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o card não for encontrado")
    void shouldThrowExceptionWhenCardNotFound() {
        // Arrange
        when(cardRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> cardService.findById(99L)
        );

        // Verify
        assertEquals("Card not found with ID: 99", exception.getMessage());
        verify(cardRepository).findById(99L);
    }

    @Test
    @DisplayName("Deve criar um card na coluna inicial")
    void shouldCreateCardInInitialColumn() {
        // Arrange
        Board board = mock(Board.class);
        BoardColumn initialColumn = mock(BoardColumn.class);

        when(boardColumnRepository.findById(1L)).thenReturn(Optional.of(initialColumn));
        // Corrigimos o tipo usado aqui
        when(initialColumn.getBoard()).thenReturn(board);
        when(board.getInitialColumn()).thenReturn(initialColumn);
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);

        Card newCard = new Card();
        newCard.setTitle("New Card");
        newCard.setDescription("New Description");

        // Act
        Card result = cardService.create(1L, newCard);

        // Assert
        assertNotNull(result);
        assertEquals("Test Card", result.getTitle());

        // Capturar o objeto Card enviado para save()
        ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardCaptor.capture());

        Card capturedCard = cardCaptor.getValue();
        assertEquals(initialColumn, capturedCard.getBoardColumn());
        assertNotNull(capturedCard.getCreatedAt());

        // Verify
        verify(boardColumnRepository).findById(1L);
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    @DisplayName("Deve atualizar um card")
    void shouldUpdateCard() {
        // Arrange
        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);

        Card updateData = new Card();
        updateData.setTitle("Updated Title");
        updateData.setDescription("Updated Description");

        // Act
        Card result = cardService.update(1L, updateData);

        // Assert
        assertNotNull(result);

        // Capturar o objeto Card enviado para save()
        ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardCaptor.capture());

        Card capturedCard = cardCaptor.getValue();
        assertEquals("Updated Title", capturedCard.getTitle());
        assertEquals("Updated Description", capturedCard.getDescription());
        assertNotNull(capturedCard.getUpdatedAt());

        // Verify
        verify(cardRepository).findById(1L);
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    @DisplayName("Deve mover um card para outra coluna")
    void shouldMoveCardToAnotherColumn() {
        // Arrange
        BoardColumn targetColumn = mock(BoardColumn.class);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));
        when(boardColumnRepository.findById(2L)).thenReturn(Optional.of(targetColumn));
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);

        // Act
        Card result = cardService.moveCard(1L, 2L);

        // Assert
        assertNotNull(result);

        // Capturar o objeto Card enviado para save()
        ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardCaptor.capture());

        Card capturedCard = cardCaptor.getValue();
        assertEquals(targetColumn, capturedCard.getBoardColumn());
        assertNotNull(capturedCard.getUpdatedAt());

        // Verify
        verify(cardRepository).findById(1L);
        verify(boardColumnRepository).findById(2L);
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar mover um card bloqueado")
    void shouldThrowExceptionWhenMovingBlockedCard() {
        // Arrange
        BoardColumn targetColumn = mock(BoardColumn.class);

        when(cardRepository.findById(2L)).thenReturn(Optional.of(blockedCard));
        when(boardColumnRepository.findById(2L)).thenReturn(Optional.of(targetColumn));

        // Act & Assert
        BlockedCardException exception = assertThrows(
            BlockedCardException.class,
            () -> cardService.moveCard(2L, 2L)
        );

        // Verify
        assertEquals("The card (ID: 2) is blocked. Operation can't be done", exception.getMessage());
        verify(cardRepository).findById(2L);
        verify(boardColumnRepository).findById(2L);
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    @DisplayName("Deve cancelar um card")
    void shouldCancelCard() {
        // Arrange
        BoardColumn cardColumn = mock(BoardColumn.class);
        Board board = mock(Board.class);
        BoardColumn cancelColumn = mock(BoardColumn.class);

        testCard.setBoardColumn(cardColumn);
        when(cardColumn.getBoard()).thenReturn(board);
        when(board.getCancelColumn()).thenReturn(cancelColumn);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);

        // Act
        Card result = cardService.cancelCard(1L);

        // Assert
        assertNotNull(result);

        // Capturar o objeto Card enviado para save()
        ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardCaptor.capture());

        Card capturedCard = cardCaptor.getValue();
        assertEquals(cancelColumn, capturedCard.getBoardColumn());
        assertNotNull(capturedCard.getUpdatedAt());

        // Verify
        verify(cardRepository).findById(1L);
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    @DisplayName("Deve desbloquear um card bloqueado ao cancelá-lo")
    void shouldUnblockCardWhenCanceling() {
        // Arrange
        BoardColumn cardColumn = mock(BoardColumn.class);
        Board board = mock(Board.class);
        BoardColumn cancelColumn = mock(BoardColumn.class);

        blockedCard.setBoardColumn(cardColumn);
        when(cardColumn.getBoard()).thenReturn(board);
        when(board.getCancelColumn()).thenReturn(cancelColumn);

        when(cardRepository.findById(2L)).thenReturn(Optional.of(blockedCard));
        when(cardRepository.save(any(Card.class))).thenReturn(blockedCard);
        // Esta é a configuração desnecessária - vamos removê-la e usar lenient
        // Alternativa: poderíamos usar o método real e verificar isBlocked() do blockedCard
        lenient().when(blockService.isCardBlocked(2L)).thenReturn(true);

        // Act
        Card result = cardService.cancelCard(2L);

        // Assert
        assertNotNull(result);

        // Verify
        verify(cardRepository).findById(2L);
        verify(blockService).unblockCard(eq(2L), eq("Card canceled"));
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    @DisplayName("Deve excluir um card")
    void shouldDeleteCard() {
        // Arrange - Nada a preparar aqui

        // Act
        cardService.delete(1L);

        // Assert & Verify - Apenas verificamos que o método foi chamado
        verify(cardRepository).deleteById(1L);
    }
}
