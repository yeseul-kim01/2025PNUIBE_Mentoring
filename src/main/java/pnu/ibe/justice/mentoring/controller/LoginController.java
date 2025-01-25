package pnu.ibe.justice.mentoring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/custom-login")
    public String login() {
        return "super/login"; // login.html 파일로 매핑
    }

}
