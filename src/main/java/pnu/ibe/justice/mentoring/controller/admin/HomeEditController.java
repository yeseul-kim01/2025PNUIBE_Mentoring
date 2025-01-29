package pnu.ibe.justice.mentoring.controller.admin;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pnu.ibe.justice.mentoring.domain.HomeEdit;
import pnu.ibe.justice.mentoring.domain.User;
import pnu.ibe.justice.mentoring.model.HomeEditDTO;
import pnu.ibe.justice.mentoring.model.MentorDTO;
import pnu.ibe.justice.mentoring.repos.HomeEditRepository;
import pnu.ibe.justice.mentoring.service.HomeEditService;
import pnu.ibe.justice.mentoring.util.CustomCollectors;
import pnu.ibe.justice.mentoring.util.WebUtils;

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

    @GetMapping("edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Integer seqId, final Model model) {
        model.addAttribute("homeEdit", homeEditService.get(seqId));
        return "admin/homeEdit/mainPage-edit";
    }

    @PostMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Integer seqId,
                       @ModelAttribute("homeEdit") @Valid final HomeEditDTO homeEditDTO,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/homeEdit/mainPage-edit";
        }
        homeEditService.update(seqId, homeEditDTO);
        System.out.println();
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("homeEdit update success"));
        return "redirect:/admin/homeEdits";
    }


}
