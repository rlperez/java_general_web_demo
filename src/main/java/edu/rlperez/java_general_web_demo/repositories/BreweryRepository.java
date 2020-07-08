package edu.rlperez.java_general_web_demo.repositories;

import edu.rlperez.java_general_web_demo.models.Brewery;
import org.springframework.data.repository.CrudRepository;

public interface BreweryRepository extends CrudRepository<Brewery, Long> {
}
