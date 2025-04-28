package br.com.devcoelho.taskboard.repository;

import br.com.devcoelho.taskboard.model.BoardColumn;
import br.com.devcoelho.taskboard.model.BoardColumnKind;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
  List<BoardColumn> findByBoardIdOrderByOrder(Long boardId);

  Optional<BoardColumn> findByBoardIdAndKind(Long boardId, BoardColumnKind kind);
}
