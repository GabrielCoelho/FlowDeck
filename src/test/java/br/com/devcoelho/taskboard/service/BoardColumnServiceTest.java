package br.com.devcoelho.taskboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.devcoelho.taskboard.exception.ColumnContainsCardException;
import br.com.devcoelho.taskboard.exception.ResourceNotFoundException;
import br.com.devcoelho.taskboard.exception.SpecialColumnDeletionException;
import br.com.devcoelho.taskboard.exception.SpecialColumnException;
import br.com.devcoelho.taskboard.model.Board;
import br.com.devcoelho.taskboard.model.BoardColumn;
import br.com.devcoelho.taskboard.model.BoardColumnKind;
import br.com.devcoelho.taskboard.model.Card;
import br.com.devcoelho.taskboard.repository.BoardColumnRepository;
import br.com.devcoelho.taskboard.repository.BoardRepository;
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
public class BoardColumnServiceTest {

  @Mock private BoardColumnRepository boardColumnRepository;

  @Mock private BoardRepository boardRepository;

  @InjectMocks private BoardColumnService boardColumnService;

  private Board testBoard;
  private BoardColumn initialColumn;
  private BoardColumn pendingColumn;
  private BoardColumn cancelColumn;
  private List<BoardColumn> boardColumns;

  @BeforeEach
  void setUp() {
    // Configuração do board de teste
    testBoard = new Board();
    testBoard.setId(1L);
    testBoard.setName("Test Board");

    // Configuração das colunas
    initialColumn = new BoardColumn();
    initialColumn.setId(1L);
    initialColumn.setName("Backlog");
    initialColumn.setKind(BoardColumnKind.INITIAL);
    initialColumn.setBoard(testBoard);
    initialColumn.setOrder(1);

    pendingColumn = new BoardColumn();
    pendingColumn.setId(2L);
    pendingColumn.setName("In Progress");
    pendingColumn.setKind(BoardColumnKind.PENDING);
    pendingColumn.setBoard(testBoard);
    pendingColumn.setOrder(2);

    cancelColumn = new BoardColumn();
    cancelColumn.setId(3L);
    cancelColumn.setName("Canceled");
    cancelColumn.setKind(BoardColumnKind.CANCEL);
    cancelColumn.setBoard(testBoard);
    cancelColumn.setOrder(3);

    // Lista de colunas
    boardColumns = new ArrayList<>();
    boardColumns.add(initialColumn);
    boardColumns.add(pendingColumn);
    boardColumns.add(cancelColumn);

    // Configurar o board com as colunas
    testBoard.setColumns(boardColumns);
  }

  @Test
  @DisplayName("Deve encontrar colunas por ID do board")
  void shouldFindColumnsByBoardId() {
    // Arrange
    when(boardColumnRepository.findByBoardIdOrderByOrder(1L)).thenReturn(boardColumns);

    // Act
    List<BoardColumn> result = boardColumnService.findByBoardId(1L);

    // Assert
    assertEquals(3, result.size());
    assertEquals(initialColumn, result.get(0));
    assertEquals(pendingColumn, result.get(1));
    assertEquals(cancelColumn, result.get(2));

    // Verify
    verify(boardColumnRepository).findByBoardIdOrderByOrder(1L);
  }

  @Test
  @DisplayName("Deve encontrar uma coluna pelo ID")
  void shouldFindColumnById() {
    // Arrange
    when(boardColumnRepository.findById(1L)).thenReturn(Optional.of(initialColumn));

    // Act
    BoardColumn result = boardColumnService.findById(1L);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Backlog", result.getName());
    assertEquals(BoardColumnKind.INITIAL, result.getKind());

    // Verify
    verify(boardColumnRepository).findById(1L);
  }

  @Test
  @DisplayName("Deve lançar exceção quando a coluna não for encontrada")
  void shouldThrowExceptionWhenColumnNotFound() {
    // Arrange
    when(boardColumnRepository.findById(99L)).thenReturn(Optional.empty());

    // Act & Assert
    ResourceNotFoundException exception =
        assertThrows(ResourceNotFoundException.class, () -> boardColumnService.findById(99L));

    // Verify
    assertEquals("Board Column not found with ID: 99", exception.getMessage());
    verify(boardColumnRepository).findById(99L);
  }

  @Test
  @DisplayName("Deve criar uma coluna padrão")
  void shouldCreateColumn() {
    // Arrange
    when(boardRepository.findById(1L)).thenReturn(Optional.of(testBoard));
    when(boardColumnRepository.findByBoardIdOrderByOrder(1L)).thenReturn(boardColumns);
    when(boardColumnRepository.save(any(BoardColumn.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    BoardColumn newColumn = new BoardColumn();
    newColumn.setName("New Column");
    newColumn.setKind(BoardColumnKind.PENDING);

    // Act
    BoardColumn result = boardColumnService.create(1L, newColumn);

    // Assert
    assertNotNull(result);
    assertEquals("New Column", result.getName());
    assertEquals(BoardColumnKind.PENDING, result.getKind());
    assertEquals(testBoard, result.getBoard());
    assertEquals(4, result.getOrder()); // Deve ser adicionada ao final

    // Verify
    verify(boardRepository).findById(1L);
    verify(boardColumnRepository).findByBoardIdOrderByOrder(1L);
    verify(boardColumnRepository).save(any(BoardColumn.class));
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar criar uma coluna especial que já existe")
  void shouldThrowExceptionWhenCreatingDuplicateSpecialColumn() {
    // Arrange
    when(boardRepository.findById(1L)).thenReturn(Optional.of(testBoard));
    when(boardColumnRepository.findByBoardIdAndKind(1L, BoardColumnKind.INITIAL))
        .thenReturn(Optional.of(initialColumn));

    BoardColumn newColumn = new BoardColumn();
    newColumn.setName("Another Initial Column");
    newColumn.setKind(BoardColumnKind.INITIAL);

    // Act & Assert
    SpecialColumnException exception =
        assertThrows(SpecialColumnException.class, () -> boardColumnService.create(1L, newColumn));

    // Verify
    verify(boardRepository).findById(1L);
    verify(boardColumnRepository).findByBoardIdAndKind(1L, BoardColumnKind.INITIAL);
    verify(boardColumnRepository, never()).save(any(BoardColumn.class));
  }

  @Test
  @DisplayName("Deve atualizar uma coluna")
  void shouldUpdateColumn() {
    // Arrange
    when(boardColumnRepository.findById(2L)).thenReturn(Optional.of(pendingColumn));
    when(boardColumnRepository.save(any(BoardColumn.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    BoardColumn updateData = new BoardColumn();
    updateData.setName("Updated Column");

    // Act
    BoardColumn result = boardColumnService.update(2L, updateData);

    // Assert
    assertNotNull(result);
    assertEquals("Updated Column", result.getName());

    // Verify
    verify(boardColumnRepository).findById(2L);
    verify(boardColumnRepository).save(any(BoardColumn.class));
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar atualizar coluna para tipo especial que já existe")
  void shouldThrowExceptionWhenUpdatingToExistingSpecialType() {
    // Arrange
    when(boardColumnRepository.findById(2L)).thenReturn(Optional.of(pendingColumn));
    when(boardColumnRepository.findByBoardIdAndKind(1L, BoardColumnKind.INITIAL))
        .thenReturn(Optional.of(initialColumn));

    BoardColumn updateData = new BoardColumn();
    updateData.setName("Updated Column");
    updateData.setKind(BoardColumnKind.INITIAL);

    // Act & Assert
    SpecialColumnException exception =
        assertThrows(SpecialColumnException.class, () -> boardColumnService.update(2L, updateData));

    // Verify
    verify(boardColumnRepository).findById(2L);
    verify(boardColumnRepository).findByBoardIdAndKind(1L, BoardColumnKind.INITIAL);
    verify(boardColumnRepository, never()).save(any(BoardColumn.class));
  }

  @Test
  @DisplayName("Deve reordenar colunas")
  void shouldReorderColumns() {
    // Arrange
    when(boardColumnRepository.findByBoardIdOrderByOrder(1L)).thenReturn(boardColumns);
    when(boardColumnRepository.save(any(BoardColumn.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    List<Long> newOrder = Arrays.asList(3L, 1L, 2L);

    // Act
    List<BoardColumn> result = boardColumnService.reorderColumns(1L, newOrder);

    // Assert
    assertNotNull(result);
    assertEquals(3, result.size());

    // Verificando as chamadas para save em ordem
    ArgumentCaptor<BoardColumn> columnCaptor = ArgumentCaptor.forClass(BoardColumn.class);
    verify(boardColumnRepository, times(3)).save(columnCaptor.capture());

    List<BoardColumn> capturedColumns = columnCaptor.getAllValues();
    // O primeiro salvo deve ser cancelColumn com ordem 1
    assertEquals(cancelColumn.getId(), capturedColumns.get(0).getId());
    assertEquals(1, capturedColumns.get(0).getOrder());

    // O segundo salvo deve ser initialColumn com ordem 2
    assertEquals(initialColumn.getId(), capturedColumns.get(1).getId());
    assertEquals(2, capturedColumns.get(1).getOrder());

    // O terceiro salvo deve ser pendingColumn com ordem 3
    assertEquals(pendingColumn.getId(), capturedColumns.get(2).getId());
    assertEquals(3, capturedColumns.get(2).getOrder());

    // Verify
    verify(boardColumnRepository, times(2)).findByBoardIdOrderByOrder(1L);
    verify(boardColumnRepository, times(3)).save(any(BoardColumn.class));
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar reordenar com lista inválida")
  void shouldThrowExceptionWhenReorderingWithInvalidList() {
    // Arrange
    when(boardColumnRepository.findByBoardIdOrderByOrder(1L)).thenReturn(boardColumns);

    List<Long> invalidOrder = Arrays.asList(1L, 2L); // Falta a coluna 3

    // Act & Assert
    RuntimeException exception =
        assertThrows(
            RuntimeException.class, () -> boardColumnService.reorderColumns(1L, invalidOrder));

    // Verify
    verify(boardColumnRepository).findByBoardIdOrderByOrder(1L);
    verify(boardColumnRepository, never()).save(any(BoardColumn.class));
  }

  @Test
  @DisplayName("Deve excluir uma coluna padrão vazia")
  void shouldDeleteEmptyPendingColumn() {
    // Arrange
    BoardColumn emptyColumn = new BoardColumn();
    emptyColumn.setId(2L);
    emptyColumn.setName("Empty Column");
    emptyColumn.setKind(BoardColumnKind.PENDING);
    emptyColumn.setCards(new ArrayList<>());

    when(boardColumnRepository.findById(2L)).thenReturn(Optional.of(emptyColumn));

    // Act
    boardColumnService.delete(2L);

    // Verify
    verify(boardColumnRepository).findById(2L);
    verify(boardColumnRepository).deleteById(2L);
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar excluir uma coluna especial")
  void shouldThrowExceptionWhenDeletingSpecialColumn() {
    // Arrange
    when(boardColumnRepository.findById(1L)).thenReturn(Optional.of(initialColumn));

    // Act & Assert
    SpecialColumnDeletionException exception =
        assertThrows(SpecialColumnDeletionException.class, () -> boardColumnService.delete(1L));

    // Verify
    verify(boardColumnRepository).findById(1L);
    verify(boardColumnRepository, never()).deleteById(any());
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar excluir uma coluna com cards")
  void shouldThrowExceptionWhenDeletingColumnWithCards() {
    // Arrange
    BoardColumn columnWithCards = new BoardColumn();
    columnWithCards.setId(2L);
    columnWithCards.setName("Column With Cards");
    columnWithCards.setKind(BoardColumnKind.PENDING);

    List<Card> cards = new ArrayList<>();
    cards.add(new Card());
    columnWithCards.setCards(cards);

    when(boardColumnRepository.findById(2L)).thenReturn(Optional.of(columnWithCards));

    // Act & Assert
    ColumnContainsCardException exception =
        assertThrows(ColumnContainsCardException.class, () -> boardColumnService.delete(2L));

    // Verify
    verify(boardColumnRepository).findById(2L);
    verify(boardColumnRepository, never()).deleteById(any());
  }

  @Test
  @DisplayName("Deve criar uma coluna normal com sucesso")
  void shouldCreateRegularColumn() {
    // Arrange
    Board board = new Board();
    board.setId(1L);

    BoardColumn newColumn = new BoardColumn();
    newColumn.setName("Nova Coluna");
    newColumn.setKind(BoardColumnKind.PENDING);
    newColumn.setOrder(0); // Zero para que o serviço defina automaticamente

    List<BoardColumn> existingColumns = Arrays.asList(initialColumn, pendingColumn, cancelColumn);

    when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
    when(boardColumnRepository.findByBoardIdOrderByOrder(1L)).thenReturn(existingColumns);
    when(boardColumnRepository.save(any(BoardColumn.class)))
        .thenAnswer(
            i -> {
              BoardColumn saved = i.getArgument(0);
              if (saved.getId() == null) {
                saved.setId(100L); // ID simulado para coluna nova
              }
              return saved;
            });

    // Act
    BoardColumn result = boardColumnService.create(1L, newColumn);

    // Assert
    assertNotNull(result);
    assertEquals("Nova Coluna", result.getName());
    assertEquals(BoardColumnKind.PENDING, result.getKind());
    assertEquals(4, result.getOrder()); // Deve ser a ordem após as existentes
    assertEquals(board, result.getBoard());
    assertEquals(100L, result.getId());

    // Verify
    verify(boardRepository).findById(1L);
    verify(boardColumnRepository).findByBoardIdOrderByOrder(1L);
    verify(boardColumnRepository).save(newColumn);
  }
}
