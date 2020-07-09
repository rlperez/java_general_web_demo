package edu.rlperez.java_general_web_demo.repositories;

import edu.rlperez.java_general_web_demo.models.BreweryType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreweryTypeRepository extends CrudRepository<BreweryType, Integer> {
}
