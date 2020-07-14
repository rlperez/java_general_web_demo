package edu.rlperez.java_general_web_demo.repositories;

import edu.rlperez.java_general_web_demo.models.BreweryType;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = {"brewery_types"})
public interface BreweryTypeRepository extends JpaRepository<BreweryType, Integer> {

    @Cacheable
    Optional<BreweryType> findByType(String type);
}
