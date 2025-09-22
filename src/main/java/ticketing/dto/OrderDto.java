package ticketing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ticketing.enums.OrderStatus;
import ticketing.enums.TicketType;

import java.time.Instant;

public record OrderDto(
        @Schema(example = "1")
        Long id,

        @Schema(example = "EVNT-ROCK-2025-2")
        String eventCode,

        @Schema(example = "STANDARD")
        TicketType ticketType,

        @Schema(example = "CONFIRMED")
        OrderStatus status,

        @Schema(example = "1")
        int quantity,

        @Schema(example = "2025-09-21T18:20:00Z")
        Instant createdAt
) {}
