package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.entities.Performance;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.repositories.ShowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/performance")
public class PerformanceController {

    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private KafkaTemplate<String, Show> kafkaTemplate;

    @GetMapping("/{idShow}/")
    @ResponseStatus(HttpStatus.OK)
    public List<Performance> getAllPerformancesFromIdShow(@PathVariable Long idShow) {
        Show show = showRepository.findById(idShow).orElse(null);
        if (Objects.nonNull(show))
            return show.getPerformances();
        return null;
    }
}
