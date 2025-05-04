package br.com.devcoelho.taskboard.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long id;
    private String name;

    @Builder.Default
    private List<BoardColumnDTO> columns = new ArrayList<>();

    // Campos adicionais para conveniência da UI
    private int totalCards;  // Número total de cards no board
}
