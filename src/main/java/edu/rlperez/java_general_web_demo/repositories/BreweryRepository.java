package edu.rlperez.java_general_web_demo.repositories;

import edu.rlperez.java_general_web_demo.models.Beer;
import org.springframework.data.repository.CrudRepository;

public interface BreweryRepository extends CrudRepository<Beer, Long> {
}
