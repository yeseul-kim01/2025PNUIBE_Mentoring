package pnu.ibe.justice.mentoring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pnu.ibe.justice.mentoring.service.HomeEditService;

@Controller
@RequestMapping("/peopleList")
public class PeopleController {

    private final HomeEditService homeEditService;

    public PeopleController (HomeEditService homeEditService) {
        this.homeEditService = homeEditService;
    }

    @ModelAttribute
    public void prepareContext(Model model){
        model.addAttribute("homeEdits",homeEditService.findAll());

    }

    @GetMapping
    public String list(final Model model, @RequestParam(value = "pSort" , required = false, defaultValue = "0") int pSort) {
        System.out.println(pSort);
        model.addAttribute("people",pSort);
        return "pages/people";
    }
}

