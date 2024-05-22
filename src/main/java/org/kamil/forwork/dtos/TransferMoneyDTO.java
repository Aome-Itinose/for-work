package org.kamil.forwork.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferMoneyDTO {
    @NotEmpty(message = "Message target user username must be not empty")
    private String targetUsername;

    private String senderUsername;

    @NotNull(message = "Amount of money must be not null")
    private Double amount;
}
