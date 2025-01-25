package pnu.ibe.justice.mentoring.controller.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pnu.ibe.justice.mentoring.domain.User;
import pnu.ibe.justice.mentoring.service.ApplicationState;

import java.awt.print.Pageable;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class HomeController {

    private final HttpSession httpSession;
    private final ApplicationState applicationState;

    @GetMapping("")
    public String index(Model model, @AuthenticationPrincipal UserDetails sessionUser) {

            if (sessionUser.getUsername().equals("admin")){
                model.addAttribute("userName",sessionUser);
            }
            else {
                return "redirect:/";
            }
        return "admin/home/index";
    }

    @GetMapping("/manage")
    public String manage(Model model, @AuthenticationPrincipal UserDetails sessionUser) {
        if (sessionUser == null) {
            return "redirect:/login"; // 인증되지 않은 사용자는 로그인 페이지로 리다이렉트
        }
        // 상태 값 전달
        model.addAttribute("userName", sessionUser);
        model.addAttribute("mentorOpenStatus", applicationState.isMentorOpenStatus()); // 현재 상태 값 전달
        return "admin/homeEdit/manage";
    }



//    // 메인 화면 - 게시판 목록
//    @GetMapping("/login")
//    public String postList(Pageable pageable, Model model) {
//        // 세션에서 사용자 정보 꺼내기
//        User user = (User) httpSession.getAttribute("user");
//        if (user != null) {
//            model.addAttribute("userName", user.getName());
//        }
//        return "admin/home/index";
//    }

    // 메인 화면 - 게시판 목록
    @GetMapping("/super")
    public String postList(Pageable pageable, Model model) {
        // 세션에서 사용자 정보 꺼내기
        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "admin/home/index";
    }



}
