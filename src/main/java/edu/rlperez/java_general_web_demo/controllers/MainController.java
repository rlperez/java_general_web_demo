package edu.rlperez.java_general_web_demo.controllers;

import edu.rlperez.java_general_web_demo.models.Brewery;
import edu.rlperez.java_general_web_demo.services.BreweryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.stream.IntStream;

@Controller
public class MainController {

    private int ROW_PER_PAGE = 50;

    private final BreweryService breweryService;

    @Autowired
    public MainController(BreweryService breweryService) {
        this.breweryService = breweryService;
    }

    @GetMapping(value = {"/", "/index"})
    public String index(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        Page<Brewery> breweryPage = breweryService.findAll(1, ROW_PER_PAGE);

        model.addAttribute("disablePrev", breweryPage.hasPrevious());
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("disableNext", !breweryPage.hasNext());
        model.addAttribute("next", pageNumber + 1);
        model.addAttribute("count", breweryPage.getTotalElements());
        model.addAttribute("totalPages", breweryPage.getTotalPages());
        model.addAttribute("currentPage", pageNumber);

        int[] pagesToShow = getPagesToShow(pageNumber, breweryPage.getTotalPages());
        model.addAttribute("pagesInNav", pagesToShow);
        model.addAttribute("padLeft", pagesToShow[1] > 5);
        model.addAttribute("padRight", pagesToShow[1] < breweryPage.getTotalPages() - 5);

        model.addAttribute("breweries", breweryService.findAll(pageNumber, ROW_PER_PAGE).getContent());

        return "index";
    }

    private int[] getPagesToShow(int currentPage, int lastPage) {
        int[] pages;
        if (currentPage <= 5) {
            pages = IntStream.rangeClosed(1, 5).toArray();
        } else if (currentPage >= lastPage - 4) {
            pages = IntStream.rangeClosed(lastPage - 4, lastPage).toArray();
        } else {
            pages = IntStream.rangeClosed(currentPage - 2, currentPage + 2).toArray();
        }

        return pages;
    }
}
