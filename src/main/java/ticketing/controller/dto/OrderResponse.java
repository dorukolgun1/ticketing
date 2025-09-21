package ticketing.controller.dto;

import java.time.Instant;

public record OrderResponse(String orderCode, String status, int quantity, Instant createdAt) { }
