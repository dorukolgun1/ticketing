package ticketing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ticketing.enums.TicketType;

public record InventoryDto(
        @Schema(example = "1")
        Long id,

        @Schema(example = "EVNT-ROCK-2025-2")
        String eventCode,

        @Schema(example = "STANDARD")
        TicketType ticketType,

        @Schema(example = "100")
        int available
) {}
