package edu.rlperez.java_general_web_demo.services;

import edu.rlperez.java_general_web_demo.models.Brewery;
import edu.rlperez.java_general_web_demo.repositories.BreweryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BreweryService {

    private final BreweryRepository breweryRepository;

    @Autowired
    public BreweryService(BreweryRepository breweryRepository) {
        this.breweryRepository = breweryRepository;
    }
    
    public Page<Brewery> findAll(int pageNumber, int rowsPerPage) {
        Pageable sortedByName = PageRequest.of(pageNumber - 1, rowsPerPage, Sort.by("name").ascending());
        return breweryRepository.findAll(sortedByName);
    }
}
