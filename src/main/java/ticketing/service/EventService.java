package ticketing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketing.mapper.EventMapper;
import ticketing.repository.EventRepository;
import ticketing.repository.entity.EventEntity;
import ticketing.service.domain.Event;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepo;
    private final EventMapper mapper;

    @Transactional
    public Event create(Event event) {
        EventEntity saved = eventRepo.save(mapper.toEntity(event));
        return mapper.toDomain(saved);
    }

    @Transactional(readOnly = true)
    public List<Event> list() {
        return eventRepo.findAll().stream().map(mapper::toDomain).toList();
    }
}
