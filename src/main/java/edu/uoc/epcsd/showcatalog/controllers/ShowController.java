package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.kafka.KafkaConstants;
import edu.uoc.epcsd.showcatalog.repositories.CategoryRepository;
import edu.uoc.epcsd.showcatalog.repositories.ShowRepository;
import edu.uoc.epcsd.showcatalog.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.entities.Performance;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.kafka.KafkaConstants;
import edu.uoc.epcsd.showcatalog.repositories.CategoryRepository;
import edu.uoc.epcsd.showcatalog.repositories.ShowRepository;
import edu.uoc.epcsd.showcatalog.service.ShowService;
import javassist.tools.rmi.ObjectNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Log4j2
@RestController
@RequestMapping("/show")
public class ShowController {
    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ShowService showService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private KafkaTemplate<String, Show> kafkaTemplate;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Show> getAllShows() {
        log.trace("getAllShows");
        return showRepository.findAll();
    }

    @PostMapping("/createShow")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Show> createShow(@RequestBody Show show) {
        log.trace("getAllCategories");
        ValidateCategories(show);
        Show showResult = showRepository.saveAndFlush(show);
        if(Objects.nonNull(showResult)){
            log.trace("Sending " + showResult + " to " + KafkaConstants.SHOW_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_ADD + " topic.");
            //Enviamos el mensaje mediante la plantilla Kafka
            kafkaTemplate.send(KafkaConstants.SHOW_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.COMMAND_ADD, showResult);
            //return new ResponseEntity<>(showResult, HttpStatus.CREATED);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(show.getId())
                    .toUri();

            return ResponseEntity.created(uri).body(show);
        }
        else throw new ResourceNotFoundException("Error!!! revisar que los parametros son correctos");

    }

    @PostMapping("/{idShow}/createPerformance")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Show> createPerformanceByIdShow(@PathVariable Long idShow ,@RequestBody Performance performance) {
        if(Objects.nonNull(idShow) && idShow != 0L){
            Show show = showRepository.findById(idShow).orElse(null);
            if(Objects.nonNull(show)){
                if(Objects.isNull(show.getPerformances()))show.setPerformances(new ArrayList<Performance>());
                show.getPerformances().add(performance);
                return new ResponseEntity<>(showRepository.save(show), HttpStatus.CREATED);
            } else throw new ResourceNotFoundException("Error!!! revisar que los parametros son correctos");
        }
        else throw new ResourceNotFoundException("Error!!! revisar que los parametros son correctos");
    }

    @PostMapping("/{idCategory}/createShow")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Show> createPerformanceByIdShow(@PathVariable Long idCategory ,@RequestBody Show show) {
        if(Objects.nonNull(idCategory) && idCategory != 0L){
            Category category = categoryRepository.findById(idCategory).orElse(null);
            if(Objects.nonNull(category)){
                if(Objects.isNull(category.getShows()))category.setShows((Set<Show>) new ArrayList<Show>());
                category.addShow(show);
                return new ResponseEntity<>(showRepository.save(show), HttpStatus.CREATED);
            } else throw new ResourceNotFoundException("Error!!! revisar que los parametros son correctos");
        }
        else throw new ResourceNotFoundException("Error!!! revisar que los parametros son correctos");
    }

   private void ValidateCategories(Show show) {
        Set<Category> list = show.getCategories();
        show.setCategories(new HashSet<>());
        list.stream().forEach(cat -> {
            if(Objects.nonNull(cat) && Objects.nonNull(cat.getId())){
                Category c = categoryRepository.getById(cat.getId());
                if(Objects.nonNull(c)){
                    c.addShow(show);
                }
            }
        });
    }

    @DeleteMapping("/delete/{idShow}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteShow(@PathVariable Long idShow) {
        log.trace("delete show : " + idShow);
        showRepository.deleteById(idShow);
        return new ResponseEntity<>("Delete show", HttpStatus.OK);
    }

    @GetMapping(path = "/getShow/{idShow}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Show> getShowById(@PathVariable Long idShow){
       return new ResponseEntity<>(showService.findById(idShow), HttpStatus.OK);
    }

    @GetMapping(path = "/consultarShow/{nameShow}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Show>> getShowByName(@PathVariable String nameShow){
        log.trace("Getting all shows name equals :" + nameShow);
        return new ResponseEntity<List<Show>>(showRepository.findShowByName(nameShow), HttpStatus.OK);
    }

    @GetMapping(path = "/shows/name")
    public ResponseEntity<List<Show>> getShowsLikeName(@RequestParam String name){
        log.trace("Getting all shows like :" + name);
        return new ResponseEntity<List<Show>>(showRepository.findByNameLike("%"+name+"%"), HttpStatus.OK);
    }

    @GetMapping("/{idShow}/PerformanceList")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Performance>> getPerformanceListByIdShow(@PathVariable Long idShow){
        Show show = showRepository.findById(idShow).orElse(null);
        log.trace("Getting all performance by idshow :" + idShow);
        return new ResponseEntity<List<Performance>>(show.getPerformances(), HttpStatus.OK);
    }

    @DeleteMapping("/{idShow}/deletePerformance")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deletePerformanceByIdShow(@PathVariable Long idShow, @RequestBody Performance performance){
        Show show = showRepository.findById(idShow).orElse(null);
        log.trace("Delete performance from idshow :" + idShow);
        if(show.getPerformances().indexOf(performance) >= 0){
            show.getPerformances().remove(performance);
            showRepository.save(show);
        }else{
            return new ResponseEntity<>("Revisa que el Show o la Actuación están escritos correctamente", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Se ha eliminado correctamente la actuación", HttpStatus.OK);
    }

}
