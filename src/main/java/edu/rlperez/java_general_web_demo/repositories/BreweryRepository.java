package edu.rlperez.java_general_web_demo.repositories;

import edu.rlperez.java_general_web_demo.models.Brewery;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BreweryRepository extends PagingAndSortingRepository<Brewery, Long> {
    Optional<Brewery> findByExternalId(long id);
}
