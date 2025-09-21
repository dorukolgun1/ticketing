package ticketing.controller.dto;

import java.time.Instant;

public record CreateEventRequest(String code, String name, Instant startTime, Instant endTime) { }
