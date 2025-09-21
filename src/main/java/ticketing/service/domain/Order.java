package ticketing.service.domain;

import lombok.Builder;
import lombok.Value;
import ticketing.service.domain.enums.OrderStatus;
import ticketing.service.domain.enums.TicketType;
import java.time.Instant;

@Value
@Builder
public class Order {
    Long id;
    String orderCode;
    String eventCode;
    TicketType ticketType;
    int quantity;
    OrderStatus status;
    Instant createdAt;
}
