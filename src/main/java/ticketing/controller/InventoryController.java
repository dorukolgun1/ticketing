package ticketing.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import ticketing.dto.InventoryDto;
import ticketing.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private final InventoryService svc;
    public InventoryController(InventoryService svc){ this.svc = svc; }

    @Operation(summary = "List inventory rows by event")
    @GetMapping("/{eventCode}")
    public List<InventoryDto> byEvent(@PathVariable("eventCode") String eventCode) { // <-- isim verildi
        return svc.getByEvent(eventCode);
    }
}
