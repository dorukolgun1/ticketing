package ticketing.domain;

import lombok.Builder;
import lombok.Value;
import ticketing.enums.OrderStatus;
import ticketing.enums.TicketType;
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
