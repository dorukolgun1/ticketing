package ticketing.dto;

import java.time.Instant;

public record EventDto(Long id, String code, String name, Instant startTime, Instant endTime) { }
