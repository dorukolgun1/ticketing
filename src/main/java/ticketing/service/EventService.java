package ticketing.service;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketing.dto.CreateEventRequest;
import ticketing.dto.EventDto;
import ticketing.entity.EventEntity;
import ticketing.mapper.EventMapper;
import ticketing.repository.EventRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Transactional
    public EventDto create(CreateEventRequest req) {
        if (eventRepository.existsByCode(req.code())) {
            throw new IllegalStateException("Event code already exists: " + req.code());
        }
        EventEntity ent = eventMapper.toEntity(eventMapper.toDomain(req));
        EventEntity saved = eventRepository.save(ent);
        return eventMapper.toDto(eventMapper.toDomain(saved));
    }

    @Transactional(readOnly = true)
    public EventDto get(String code) {
        EventEntity ent = eventRepository.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("Event not found: " + code));
        return eventMapper.toDto(eventMapper.toDomain(ent));
    }
}
