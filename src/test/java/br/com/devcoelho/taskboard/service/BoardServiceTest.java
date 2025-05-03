package br.com.devcoelho.taskboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.devcoelho.taskboard.exception.ResourceNotFoundException;
import br.com.devcoelho.taskboard.model.Board;
import br.com.devcoelho.taskboard.repository.BoardColumnRepository;
import br.com.devcoelho.taskboard.repository.BoardRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

  @Mock private BoardRepository boardRepository;

  @Mock private BoardColumnRepository boardColumnRepository;

  @InjectMocks private BoardService boardService;

  private Board testBoard;
  private Board testBoard2;

  @BeforeEach
  void setUp() {
    testBoard = new Board();
    testBoard.setId(1L);
    testBoard.setName("Test Board");

    testBoard2 = new Board();
    testBoard2.setId(2L);
    testBoard2.setName("Test Board 2");
  }

  // [Testes anteriores estão aqui]

  @Test
  @DisplayName("Deve retornar todos os boards")
  void shouldFindAllBoards() {
    // Arrange
    List<Board> expectedBoards = Arrays.asList(testBoard, testBoard2);
    when(boardRepository.findAll()).thenReturn(expectedBoards);

    // Act
    List<Board> result = boardService.findAll();

    // Assert
    assertEquals(2, result.size());
    assertTrue(result.contains(testBoard));
    assertTrue(result.contains(testBoard2));

    // Verify
    verify(boardRepository).findAll();
  }

  @Test
  @DisplayName("Deve excluir um board")
  void shouldDeleteBoard() {
    // Arrange - Neste caso, não precisamos configurar nenhum comportamento específico

    // Act
    boardService.delete(1L);

    // Assert & Verify - Verificamos apenas que o método deleteById foi chamado corretamente
    verify(boardRepository).deleteById(1L);
  }

  @Test
  @DisplayName("Deve lançar exceção quando o board não for encontrado")
  void shouldThrowExceptionWhenBoardNotFound() {
    // Arrange
    when(boardRepository.findById(99L)).thenReturn(Optional.empty());

    // Act & Assert
    ResourceNotFoundException exception =
        assertThrows(ResourceNotFoundException.class, () -> boardService.findById(99L));

    // Verifique a mensagem da exceção
    assertEquals("Board not found with ID: 99", exception.getMessage());

    // Verify
    verify(boardRepository).findById(99L);
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar atualizar um board inexistente")
  void shouldThrowExceptionWhenUpdatingNonExistentBoard() {
    // Arrange
    when(boardRepository.findById(99L)).thenReturn(Optional.empty());
    Board updateData = new Board();
    updateData.setName("Updated Name");

    // Act & Assert
    ResourceNotFoundException exception =
        assertThrows(ResourceNotFoundException.class, () -> boardService.update(99L, updateData));

    // Verifique a mensagem da exceção
    assertEquals("Board not found with ID: 99", exception.getMessage());

    // Verify
    verify(boardRepository).findById(99L);
    // Garantir que o método save nunca foi chamado
    verify(boardRepository, never()).save(any(Board.class));
  }
}
