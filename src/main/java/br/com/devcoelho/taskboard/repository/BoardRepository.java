package br.com.devcoelho.taskboard.repository;

import br.com.devcoelho.taskboard.model.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
  // Busca boards pelo nome contendo o texto
  List<Board> findByNameContainingIgnoreCase(String name);
}
