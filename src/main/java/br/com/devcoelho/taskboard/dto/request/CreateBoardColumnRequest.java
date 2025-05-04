package br.com.devcoelho.taskboard.dto.request;

import br.com.devcoelho.taskboard.model.BoardColumnKind;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateBoardColumnRequest {
  @NotBlank(message = "Column Name is Obligatory")
  @Size(min = 3, max = 100, message = "Column Name must have 3 <= length <= 100")
  private String name;

  @NotNull(message = "Column Kind is Obligatory")
  private BoardColumnKind kind;

  private int order;
}
