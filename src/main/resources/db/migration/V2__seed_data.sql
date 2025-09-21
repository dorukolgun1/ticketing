INSERT INTO event(code, name, start_time, end_time)
VALUES ('EVNT-ROCK-2025','Rock Fest', now() + interval '7 day', now() + interval '7 day' + interval '3 hour')
    ON CONFLICT (code) DO NOTHING;

INSERT INTO inventory(event_code, ticket_type, total, sold)
VALUES
    ('EVNT-ROCK-2025','VIP',      200, 0),
    ('EVNT-ROCK-2025','STANDARD', 5000, 0),
    ('EVNT-ROCK-2025','STUDENT',  800, 0)
    ON CONFLICT ON CONSTRAINT inventory_uc DO NOTHING;
