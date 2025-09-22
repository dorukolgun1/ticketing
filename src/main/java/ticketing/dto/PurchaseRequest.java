package ticketing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ticketing.enums.TicketType;

public record PurchaseRequest(
        @Schema(example = "EVNT-ROCK-2025-2")
        @NotBlank
        String eventCode,

        @Schema(example = "STANDARD")
        @NotNull
        TicketType ticketType,

        @Schema(example = "1")
        @Min(1)
        int quantity
) {}
