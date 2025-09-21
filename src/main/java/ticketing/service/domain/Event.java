package ticketing.service.domain;

import lombok.Builder;
import lombok.Value;
import java.time.Instant;

@Value
@Builder
public class Event {
    Long id;
    String code;
    String name;
    Instant startTime;
    Instant endTime;
}
