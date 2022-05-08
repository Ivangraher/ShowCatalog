package edu.uoc.epcsd.showcatalog.service;

import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.repositories.ShowRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ShowService {

    private ShowRepository showRepository;

    public List<Show> listAllShows() {
        return showRepository.findAll();
    }

    public List<Show> findShowByName(String name){
        return null;
    }

    public Show findById(Long id) {
        return showRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public List<Show> getShowsLikeName(String name) {
        return showRepository.findShowByName(name);
    }

    public List<Show> findByNameLike(String name) {
        return showRepository.findByNameLike(name);
    }

}
