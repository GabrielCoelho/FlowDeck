package br.com.devcoelho.taskboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateBoardRequest {
  @NotBlank(message = "Board name is obligatory")
  @Size(min = 3, max = 100, message = "Name must be 3 <= length <= 100")
  private String name;
}
