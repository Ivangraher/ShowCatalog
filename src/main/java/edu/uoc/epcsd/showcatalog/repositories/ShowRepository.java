package edu.uoc.epcsd.showcatalog.repositories;

import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.entities.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {

    Show findByNameEquals(String name);
    List<Show> findShowByName(String name);
    List<Show> findByNameLike(String name);

}
