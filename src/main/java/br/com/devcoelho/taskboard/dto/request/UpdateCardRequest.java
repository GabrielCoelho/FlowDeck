package br.com.devcoelho.taskboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCardRequest {
  @NotBlank(message = "Card Title is obligatory")
  @Size(min = 3, max = 100, message = "Title must have 3 <= length <= 100")
  private String title;

  private String description;
}
