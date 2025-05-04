package br.com.devcoelho.taskboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BlockCardRequest {
  @NotBlank(message = "Block reason is obligatory")
  private String reason;
}
