package edu.uoc.epcsd.showcatalog.repositories;

import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.entities.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceRepository extends JpaRepository<Category, Long> {

    List<Performance> findPerformanceByName(String name);
    List<Performance> findByNameLike(String name);
}
