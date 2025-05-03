package br.com.devcoelho.taskboard.dao;

import br.com.devcoelho.taskboard.model.Board;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO para operações de acesso a dados relacionadas à entidade Board.
 * Fornece métodos específicos não cobertos pela interface JpaRepository.
 */
public interface BoardDAO {

    /**
     * Busca boards mais ativos com base na quantidade de movimentações de cards.
     *
     * @param limit número máximo de boards a retornar
     * @return lista de boards ordenados por atividade
     */
    List<Board> findMostActiveBoards(int limit);

    /**
     * Busca boards pelo nome contendo o texto e pela quantidade mínima de colunas.
     *
     * @param name parte do nome a ser buscada
     * @param minColumns número mínimo de colunas que o board deve ter
     * @return lista de boards que atendem aos critérios
     */
    List<Board> findByNameAndMinColumns(String name, int minColumns);

    /**
     * Busca board com todas as suas relações carregadas (colunas e cards).
     *
     * @param id identificador do board
     * @return o board com todas as relações, se existir
     */
    Optional<Board> findByIdWithAllRelations(Long id);

    /**
     * Conta o número total de cards em um board.
     *
     * @param boardId identificador do board
     * @return número total de cards
     */
    int countTotalCards(Long boardId);
}
