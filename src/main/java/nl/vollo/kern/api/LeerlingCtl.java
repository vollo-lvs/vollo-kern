package nl.vollo.kern.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import nl.vollo.kern.model.Geslacht;
import nl.vollo.kern.model.Leerling;
import nl.vollo.kern.repository.LeerlingRepository;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Api(value = "Leerling")
@RestController()
@RequestMapping("/leerling")
@Log4j2
public class LeerlingCtl {

    @Autowired
    LeerlingRepository leerlingRepository;

    @GetMapping(produces = "application/json")
    public List<Leerling> listAll() {
        return leerlingRepository.findAll();
    }

    @ApiOperation(value = "Maak en retourneer een voorbeeldleerling met willekeurig data.")
    @GetMapping(value = "/sample", produces = "application/json")
    public ResponseEntity<Leerling> sampleLeerling() {
        Leerling leerling = new Leerling();
        leerling.setAchternaam(randomAlphabetic(40));
        leerling.setVoornamen(randomAlphabetic(40));
        leerling.setRoepnaam(randomAlphabetic(20));
        leerling.setGeboortedatum(DateUtils.addDays(new Date(), -RandomUtils.nextInt(365 * 6, 365 * 12)));
        if (Math.random() > .67) {
            leerling.setGeslacht(Geslacht.MAN);
        } else if (Math.random() > .5) {
            leerling.setGeslacht(Geslacht.VROUW);
        } else {
            leerling.setGeslacht(Geslacht.OVERIG);
        }
        leerling = leerlingRepository.save(leerling);
        log.info("Leerling aangemaakt: {}", leerling);
        return new ResponseEntity<>(leerling, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Leerling> create(Leerling entity) {
        entity = leerlingRepository.save(entity);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id:[0-9][0-9]*}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        return leerlingRepository
                .findById(id)
                .map(leerling -> {
                    leerlingRepository.deleteById(id);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/{id:[0-9][0-9]*}", produces = "application/json")
    public ResponseEntity<Leerling> findById(@PathVariable("id") Long id) {
        return leerlingRepository
                .findById(id)
                .map(leerling -> new ResponseEntity<>(leerling, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(value = "/{id:[0-9][0-9]*}", consumes = "application/json")
    public ResponseEntity<?> update(@PathVariable("id") Long id, final Leerling entity) {
        if (entity == null || id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!id.equals(entity.getId())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return leerlingRepository
                .findById(id)
                .map(entity1 -> {
                    leerlingRepository.save(entity);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
