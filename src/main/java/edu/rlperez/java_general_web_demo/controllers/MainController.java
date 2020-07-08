package edu.rlperez.java_general_web_demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
public class MainController {

    @GetMapping("/")
    public ModelAndView index(Model model) {
        return new ModelAndView("index", new HashMap<>());
    }

}
