package nl.vollo.kern;

import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class DatumService {
    public ZonedDateTime now() {
        return ZonedDateTime.now();
    }
}
