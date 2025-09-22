package ticketing.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ticketing.dto.OrderDto;
import ticketing.dto.PurchaseRequest;
import ticketing.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @Operation(summary = "Purchase tickets")
    @PostMapping("/purchase")
    public OrderDto purchase(@RequestHeader(name = "Idempotency-Key") String idemKey,     // <-- isim verildi
                             @RequestParam(name = "strategy", defaultValue = "optimistic") String strategy,
                             @RequestBody @Valid PurchaseRequest req) {
        return orderService.purchase(req, idemKey, strategy);
    }

    @Operation(summary = "List orders of event with pagination")
    @GetMapping
    public Page<OrderDto> list(@RequestParam(name = "event") String event,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "20") int size) {
        return orderService.list(event, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public OrderDto get(@PathVariable("id") long id) {   // <-- isim verildi
        return orderService.get(id);
    }
}
