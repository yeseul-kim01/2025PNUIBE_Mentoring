package pnu.ibe.justice.mentoring.controller.Super;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pnu.ibe.justice.mentoring.config.auth.LoginUser;
import pnu.ibe.justice.mentoring.config.auth.SessionUser;
import pnu.ibe.justice.mentoring.domain.User;

import java.awt.print.Pageable;

@Controller
@RequiredArgsConstructor
@RequestMapping("/super")
public class SuperHomeController {

    private final HttpSession httpSession;

    @GetMapping("")
    public String index() {
        return "super/login";
    }

    @ModelAttribute("user")
    public SessionUser getSettings(@LoginUser SessionUser user) {
        return user;
    }

    @GetMapping("/login")
    public String login() {
        return "super/login";
    }

//    @GetMapping("/super")
//    public String postList(Pageable pageable, Model model) {
//        // 세션에서 사용자 정보 꺼내기
//        User user = (User) httpSession.getAttribute("user");
//        if (user != null) {
//            model.addAttribute("userName", user.getName());
//        }
//        return "admin/home/index";
//    }
//
//

}
