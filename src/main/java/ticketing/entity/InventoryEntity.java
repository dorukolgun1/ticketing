package ticketing.entity;

import jakarta.persistence.*;
import lombok.*;
import ticketing.enums.TicketType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "inventory",
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_inventory_event_ticket", columnNames = {"event_code", "ticket_type"})
        },
        indexes = {
                @Index(name = "idx_inventory_event_code", columnList = "event_code")
        }
)
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_code", nullable = false, length = 64)
    private String eventCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type", nullable = false, length = 32)
    private TicketType ticketType; // STANDARD / VIP / STUDENT

    @Column(name = "total", nullable = false)
    private int total;

    @Column(name = "sold", nullable = false)
    private int sold;

    /** Optimistic locking için. Flyway'de tabloya version kolonu varsa JPA yönetir. */
    @Version
    @Column(name = "version")
    private Long version;

    /** Kullanışlı yardımcı: kalan stok */
    @Transient
    public int getRemaining() {
        return total - sold;
    }
}
