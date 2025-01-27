package pnu.ibe.justice.mentoring.controller.admin;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pnu.ibe.justice.mentoring.domain.HomeEdit;
import pnu.ibe.justice.mentoring.domain.User;
import pnu.ibe.justice.mentoring.repos.HomeEditRepository;
import pnu.ibe.justice.mentoring.service.HomeEditService;
import pnu.ibe.justice.mentoring.util.CustomCollectors;

import static pnu.ibe.justice.mentoring.util.CustomCollectors.toSortedMap;

@Controller
@RequestMapping("/admin/homeEdits")
public class HomeEditController {
    private final HomeEditService homeEditService;
    private final HomeEditRepository homeEditRepository;

    public HomeEditController (HomeEditService homeEditService,
                               HomeEditRepository homeEditRepository) {
        this.homeEditService = homeEditService;
        this.homeEditRepository = homeEditRepository;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("homeEdits", homeEditService.findAll());
        return "admin/homeEdit/home-Edit";
    }

}
