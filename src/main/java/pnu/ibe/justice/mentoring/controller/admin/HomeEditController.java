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
import pnu.ibe.justice.mentoring.model.*;
import pnu.ibe.justice.mentoring.repos.HomeEditRepository;
import pnu.ibe.justice.mentoring.repos.ManagerRepository;
import pnu.ibe.justice.mentoring.service.HomeEditService;
import pnu.ibe.justice.mentoring.service.ManagerService;
import pnu.ibe.justice.mentoring.util.CustomCollectors;
import pnu.ibe.justice.mentoring.util.WebUtils;

import java.util.Arrays;

import static pnu.ibe.justice.mentoring.util.CustomCollectors.toSortedMap;

@Controller
@RequestMapping("/admin/homeEdits")
public class HomeEditController {
    private final HomeEditService homeEditService;
    private final HomeEditRepository homeEditRepository;
    private final ManagerService managerService;



    public HomeEditController (HomeEditService homeEditService,
                               HomeEditRepository homeEditRepository,
                               ManagerService managerService) {
        this.homeEditService = homeEditService;
        this.homeEditRepository = homeEditRepository;
        this.managerService = managerService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("homeEdits", homeEditService.findAll());
        model.addAttribute("managementTeam",managerService.findAll());
        return "admin/homeEdit/home-Edit";
    }

    @GetMapping("/managementTeam")
    public String managementTeamlist(final Model model) {
        model.addAttribute("managementTeams", managerService.findAll());
        return "admin/homeEdit/managementTeam-list";
    }


    @GetMapping("/managementTeam/add")
    public String managementTeamAdd(@ModelAttribute("managementTeam") final ManagerDTO managerDTO,Model model) {
        model.addAttribute("grades", Arrays.asList(Grade.values())); // Enum 목록 전달
        return "admin/homeEdit/managementTeam-add";
    }

    @PostMapping("/managementTeam/add")
    public String managementTeamAdd(@ModelAttribute("managementTeam") @Valid final ManagerDTO managerDTO, RedirectAttributes redirectAttributes) {
        managerService.create(managerDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("운영진이 성공적으로 추가되었습니다."));
        return  "redirect:/admin/homeEdits/managementTeam";
    }

    @GetMapping("/managementTeam/edit/{seqId}")
    public String managementTeamEdit(@PathVariable(name = "seqId") final Integer seqId,Model model) {
        model.addAttribute("managementTeam",managerService.get(seqId));
        model.addAttribute("gradeValues", Grade.values());
        return "admin/homeEdit/managementTeam-edit";
    }

    @PostMapping("/managementTeam/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Integer seqId,
                       @ModelAttribute("managementTeam") @Valid final ManagerDTO managerDTO,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/homeEdit/managementTeam-edit";
        }
        managerService.update(seqId, managerDTO);

        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("운영진이 업데이트 되었습니다."));
        return "redirect:/admin/homeEdits/managementTeam";
    }

    @PostMapping("/managementTeam/delete/{seqId}")
    public String delete(@PathVariable(name = "seqId") final Integer seqId,
                         final RedirectAttributes redirectAttributes) {
        managerService.delete(seqId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("운영진이 삭제되었습니다."));
        return "redirect:/admin/homeEdits/managementTeam";
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
