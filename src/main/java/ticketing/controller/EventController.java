package ticketing.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ticketing.dto.CreateEventRequest;
import ticketing.dto.EventDto;
import ticketing.service.EventService;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService svc;

    public EventController(EventService svc) {
        this.svc = svc;
    }

    @Operation(summary = "Create event")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto create(@RequestBody @Valid CreateEventRequest req) {
        return svc.create(req);
    }

    @Operation(summary = "Get event by code")
    @GetMapping("/{code}")
    public EventDto get(@PathVariable("code") String code) {   // <-- isim verildi
        return svc.get(code);
    }
}
