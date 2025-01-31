package pnu.ibe.justice.mentoring.controller;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pnu.ibe.justice.mentoring.domain.User;
import pnu.ibe.justice.mentoring.service.HomeEditService;
import pnu.ibe.justice.mentoring.util.CustomCollectors;

@Controller
@RequestMapping("/introduce")
public class IntroduceController {

    private final HomeEditService homeEditService;


    public IntroduceController(HomeEditService homeEditService){
        this.homeEditService = homeEditService;

    }
    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("homeEdits",homeEditService.findAll());
    }


    @GetMapping
    public String home() {
        System.out.println("success introduce connect");
        return "pages/introduce";
    }
}