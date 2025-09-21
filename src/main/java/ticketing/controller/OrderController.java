package ticketing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.controller.dto.OrderResponse;
import ticketing.controller.dto.PurchaseRequest;
import ticketing.service.OrderService;
import ticketing.service.domain.Order;
import ticketing.service.domain.enums.TicketType;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/purchase")
    public ResponseEntity<OrderResponse> purchase(
            @RequestHeader("Idempotency-Key") String idemKey,
            @RequestParam(defaultValue = "optimistic") String strategy,
            @RequestBody PurchaseRequest req) {

        var type = TicketType.valueOf(req.ticketType());
        Order order = switch (strategy) {
            case "pessimistic" -> orderService.purchasePessimistic(idemKey, req.eventCode(), type, req.quantity());
            default -> orderService.purchaseOptimistic(idemKey, req.eventCode(), type, req.quantity());
        };

        return ResponseEntity.ok(new OrderResponse(order.getOrderCode(), order.getStatus().name(),
                order.getQuantity(), order.getCreatedAt()));
    }
}
