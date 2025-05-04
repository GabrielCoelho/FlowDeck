package br.com.devcoelho.taskboard.service;

import br.com.devcoelho.taskboard.dto.BoardDTO;
import br.com.devcoelho.taskboard.dto.BoardSummaryDTO;
import br.com.devcoelho.taskboard.dto.CardDTO;
import br.com.devcoelho.taskboard.dto.CardSummaryDTO;
import br.com.devcoelho.taskboard.dto.BoardColumnDTO;
import br.com.devcoelho.taskboard.dto.BlockDTO;
import br.com.devcoelho.taskboard.dto.mappers.BoardMapper;
import br.com.devcoelho.taskboard.dto.mappers.CardMapper;
import br.com.devcoelho.taskboard.dto.mappers.BoardColumnMapper;
import br.com.devcoelho.taskboard.dto.mappers.BlockMapper;
import br.com.devcoelho.taskboard.dto.request.CreateBoardRequest;
import br.com.devcoelho.taskboard.dto.request.CreateCardRequest;
import br.com.devcoelho.taskboard.dto.request.CreateBoardColumnRequest;
import br.com.devcoelho.taskboard.model.Board;
import br.com.devcoelho.taskboard.model.Card;
import br.com.devcoelho.taskboard.model.BoardColumn;
import br.com.devcoelho.taskboard.model.Block;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Serviço responsável por operações de conversão entre DTOs e entidades.
 * Centraliza a lógica de mapeamento para facilitar operações mais complexas e personalizadas.
 */
@Service
@RequiredArgsConstructor
public class DtoConversionService {

    private final BoardMapper boardMapper;
    private final CardMapper cardMapper;
    private final BoardColumnMapper boardColumnMapper;
    private final BlockMapper blockMapper;

    // --- Métodos para Board ---

    /**
     * Converte uma entidade Board para BoardDTO com todas as relações.
     */
    public BoardDTO convertBoardToFullDto(Board board) {
        return boardMapper.toDto(board);
    }

    /**
     * Converte uma entidade Board para BoardSummaryDTO (versão resumida).
     */
    public BoardSummaryDTO convertBoardToSummaryDto(Board board) {
        return boardMapper.toSummaryDto(board);
    }

    /**
     * Converte uma lista de entidades Board para lista de BoardDTO.
     */
    public List<BoardDTO> convertBoardsToFullDtos(List<Board> boards) {
        return boardMapper.toDtoList(boards);
    }

    /**
     * Converte uma lista de entidades Board para lista de BoardSummaryDTO.
     */
    public List<BoardSummaryDTO> convertBoardsToSummaryDtos(List<Board> boards) {
        return boardMapper.toSummaryDtoList(boards);
    }

    /**
     * Cria uma nova entidade Board a partir de uma requisição de criação.
     */
    public Board createBoardFromRequest(CreateBoardRequest request) {
        Board board = new Board();
        board.setName(request.getName());
        return board;
    }

    // --- Métodos para Card ---

    /**
     * Converte uma entidade Card para CardDTO com todas as relações.
     */
    public CardDTO convertCardToFullDto(Card card) {
        return cardMapper.toDto(card);
    }

    /**
     * Converte uma entidade Card para CardSummaryDTO (versão resumida).
     */
    public CardSummaryDTO convertCardToSummaryDto(Card card) {
        return cardMapper.toSummaryDto(card);
    }

    /**
     * Converte uma lista de entidades Card para lista de CardDTO.
     */
    public List<CardDTO> convertCardsToFullDtos(List<Card> cards) {
        return cardMapper.toDtoList(cards);
    }

    /**
     * Converte uma lista de entidades Card para lista de CardSummaryDTO.
     */
    public List<CardSummaryDTO> convertCardsToSummaryDtos(List<Card> cards) {
        return cardMapper.toSummaryDtoList(cards);
    }

    /**
     * Cria uma nova entidade Card a partir de uma requisição de criação.
     */
    public Card createCardFromRequest(CreateCardRequest request) {
        Card card = new Card();
        card.setTitle(request.getTitle());
        card.setDescription(request.getDescription());
        return card;
    }

    // --- Métodos para BoardColumn ---

    /**
     * Converte uma entidade BoardColumn para BoardColumnDTO com todas as relações.
     */
    public BoardColumnDTO convertBoardColumnToDto(BoardColumn boardColumn) {
        return boardColumnMapper.toDto(boardColumn);
    }

    /**
     * Converte uma entidade BoardColumn para BoardColumnDTO sem incluir os cards.
     */
    public BoardColumnDTO convertBoardColumnToDtoWithoutCards(BoardColumn boardColumn) {
        return boardColumnMapper.toDtoWithoutCards(boardColumn);
    }

    /**
     * Converte uma lista de entidades BoardColumn para lista de BoardColumnDTO.
     */
    public List<BoardColumnDTO> convertBoardColumnsToDtos(List<BoardColumn> boardColumns) {
        return boardColumnMapper.toDtoList(boardColumns);
    }

    /**
     * Cria uma nova entidade BoardColumn a partir de uma requisição de criação.
     */
    public BoardColumn createBoardColumnFromRequest(CreateBoardColumnRequest request) {
        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setName(request.getName());
        boardColumn.setKind(request.getKind());
        boardColumn.setOrder(request.getOrder());
        return boardColumn;
    }

    // --- Métodos para Block ---

    /**
     * Converte uma entidade Block para BlockDTO.
     */
    public BlockDTO convertBlockToDto(Block block) {
        return blockMapper.toDto(block);
    }

    /**
     * Converte uma lista de entidades Block para lista de BlockDTO.
     */
    public List<BlockDTO> convertBlocksToDtos(List<Block> blocks) {
        return blockMapper.toDtoList(blocks);
    }

    // --- Métodos especializados ---

    /**
     * Converte um Board completo em um mapa de BoardColumnDTO por ID.
     * Útil para operações de frontend como reorganização de colunas.
     */
    public Map<Long, BoardColumnDTO> getColumnMapForBoard(Board board) {
        if (board == null || board.getColumns() == null) {
            return Map.of();
        }

        return board.getColumns().stream()
            .map(boardColumnMapper::toDto)
            .collect(Collectors.toMap(BoardColumnDTO::getId, Function.identity()));
    }

    /**
     * Cria um mapa de CardDTO por ID para um conjunto de cards.
     * Útil para operações de frontend como reorganização de cards.
     */
    public Map<Long, CardDTO> getCardMapForColumns(List<BoardColumn> columns) {
        if (columns == null) {
            return Map.of();
        }

        return columns.stream()
            .flatMap(column -> column.getCards().stream())
            .map(cardMapper::toDto)
            .collect(Collectors.toMap(CardDTO::getId, Function.identity()));
    }

    /**
     * Obtém estatísticas de um Board como um BoardSummaryDTO sem buscar dados completos.
     * Útil para dashboards e visões gerais.
     */
    public BoardSummaryDTO getBoardStats(Board board) {
        return boardMapper.toSummaryDto(board);
    }
}
