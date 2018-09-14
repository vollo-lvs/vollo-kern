package nl.vollo.kern.api;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import nl.vollo.kern.model.Score;
import nl.vollo.kern.repository.LeerlingRepository;
import nl.vollo.kern.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "Score")
@RestController
@RequestMapping("/score")
@Log4j2
public class ScoreCtl {

    @Autowired
    LeerlingRepository leerlingRepository;

    @Autowired
    ScoreRepository scoreRepository;

    @GetMapping()
    public ResponseEntity<List<Score>> getScores(@RequestParam(name = "leerlingId") Long leerlingId) {
        return leerlingRepository.findById(leerlingId)
                .map(leerling -> scoreRepository.findAllByLeerling(leerling))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
