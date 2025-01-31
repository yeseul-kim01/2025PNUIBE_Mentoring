package pnu.ibe.justice.mentoring.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pnu.ibe.justice.mentoring.config.auth.LoginUser;
import pnu.ibe.justice.mentoring.config.auth.SessionUser;
import pnu.ibe.justice.mentoring.domain.User;
import pnu.ibe.justice.mentoring.model.Role;
import pnu.ibe.justice.mentoring.model.UserDTO;
import pnu.ibe.justice.mentoring.repos.UserRepository;
import pnu.ibe.justice.mentoring.service.HomeEditService;
import pnu.ibe.justice.mentoring.service.UserService;
import pnu.ibe.justice.mentoring.util.ReferencedWarning;
import pnu.ibe.justice.mentoring.util.WebUtils;
import pnu.ibe.justice.mentoring.service.UserService;
import pnu.ibe.justice.mentoring.util.CustomCollectors;

import java.net.InterfaceAddress;


@Controller
@RequestMapping("/MemberIfForm")
public class MemberInformationController {

    private final UserService userService;
    private final HomeEditService homeEditService;


    @ModelAttribute("user")
    public SessionUser getSettings(@LoginUser SessionUser user) {
        return user;
    }

    public MemberInformationController(final UserService userService, final HomeEditService homeEditService ){
        this.userService = userService;
        this.homeEditService = homeEditService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("roleValues", Role.values());
        model.addAttribute("homeEdits",homeEditService.findAll());
    }

    @GetMapping("/edit")
    public String edit(@LoginUser SessionUser sessionUser,final Model model) {
        model.addAttribute("modifyUser", userService.get(sessionUser.getSeqId()));
        return "pages/MemberIfForm";
    }

    @PostMapping("/edit")
    public String edit(
            @ModelAttribute("modifyUser") @Valid final UserDTO userDTO, @LoginUser SessionUser sessionUser,final BindingResult bindingResult,
                       final RedirectAttributes redirectAttributes) {
        userService.update(sessionUser.getSeqId(), userDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.update.success"));
        return "redirect:/";
    }
}
