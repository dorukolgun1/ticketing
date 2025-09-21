package ticketing.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import ticketing.service.domain.enums.TicketType;

@Entity
@Table(name = "inventory",
        uniqueConstraints = @UniqueConstraint(columnNames = {"event_code","ticket_type"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class InventoryEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_code", nullable = false)
    private String eventCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type", nullable = false)
    private TicketType ticketType;

    @Column(nullable = false)
    private int total;

    @Column(nullable = false)
    private int sold;

    @Version
    private Long version;
}
