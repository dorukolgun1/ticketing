package ticketing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketing.controller.dto.CreateEventRequest;
import ticketing.controller.dto.EventDto;
import ticketing.mapper.EventMapper;
import ticketing.service.EventService;
import ticketing.service.InventoryService;
import ticketing.service.domain.Event;
import ticketing.service.domain.enums.TicketType;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final InventoryService inventoryService;
    private final EventMapper mapper;

    @PostMapping
    public ResponseEntity<EventDto> create(@RequestBody CreateEventRequest req) {
        Event created = eventService.create(Event.builder()
                .id(null).code(req.code()).name(req.name())
                .startTime(req.startTime()).endTime(req.endTime()).build());


        inventoryService.createIfNotExists(created.getCode(), TicketType.VIP, 200);
        inventoryService.createIfNotExists(created.getCode(), TicketType.STANDARD, 5000);
        inventoryService.createIfNotExists(created.getCode(), TicketType.STUDENT, 800);

        return ResponseEntity.ok(mapper.toDto(created));
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> list() {
        return ResponseEntity.ok(eventService.list().stream().map(mapper::toDto).toList());
    }
}
