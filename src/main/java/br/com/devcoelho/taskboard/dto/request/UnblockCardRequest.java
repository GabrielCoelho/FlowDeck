package br.com.devcoelho.taskboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UnblockCardRequest {
  @NotBlank(message = "Unblock reason is obligatory")
  private String reason;
}
