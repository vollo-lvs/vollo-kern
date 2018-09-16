package nl.vollo.kern;

import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
class DatumService {
    fun now(): ZonedDateTime = ZonedDateTime.now()
}
