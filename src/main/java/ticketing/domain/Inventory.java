package ticketing.domain;

import lombok.Builder;
import lombok.Value;
import ticketing.enums.TicketType;


@Value
@Builder
public class Inventory {
    Long id;
    String eventCode;
    TicketType ticketType;
    int total;
    int sold;

    // Optimistic locking için varsa kullanılır; yoksa mapper'da ignore ediyoruz.
    Long version;
}
