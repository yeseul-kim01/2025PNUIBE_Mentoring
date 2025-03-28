package pnu.ibe.justice.mentoring.controller.admin;

import jakarta.validation.Valid;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pnu.ibe.justice.mentoring.config.auth.LoginUser;
import pnu.ibe.justice.mentoring.config.auth.SessionUser;

import pnu.ibe.justice.mentoring.domain.User;
import pnu.ibe.justice.mentoring.model.MentorDTO;
import pnu.ibe.justice.mentoring.model.MentorFileDTO;
import pnu.ibe.justice.mentoring.repos.UserRepository;
import pnu.ibe.justice.mentoring.service.ApplicationState;
import pnu.ibe.justice.mentoring.service.MentorFileService;
import pnu.ibe.justice.mentoring.service.MentorService;
import pnu.ibe.justice.mentoring.util.CustomCollectors;
import pnu.ibe.justice.mentoring.util.ReferencedWarning;
import pnu.ibe.justice.mentoring.util.WebUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Controller
@RequestMapping("/admin/mentors")
public class MentorController {

//    @ModelAttribute("sessionuser")
//    public SessionUser getSettings(@LoginUser SessionUser user) {
//        System.out.println("success mentorcontroller session user connection");
//        System.out.println(user.getSeqId());
//        return user;
//    }

    private final MentorService mentorService;
    private final UserRepository userRepository;
    private final MentorFileService mentorFileService;
    private String uploadFolder = "/Users/gim-yeseul/Desktop/mentoring_pj/mentoring/upload/";
    private final ApplicationState applicationState;


    public MentorController(final MentorService mentorService,
                            final UserRepository userRepository, MentorFileService mentorFileService,
                            ApplicationState applicationState) {
        this.mentorService = mentorService;
        this.userRepository = userRepository;
        this.mentorFileService = mentorFileService;
        this.applicationState = applicationState;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("usersValues", userRepository.findAll(Sort.by("seqId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getSeqId, User::getEmail)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("mentors", mentorService.findAll());
        return "admin/mentor/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("mentor") final MentorDTO mentorDTO, final Model model, @LoginUser SessionUser sessionUser) {
        return "admin/mentor/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("mentor") @Valid final MentorDTO mentorDTO, @LoginUser SessionUser sessionUser,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/mentor/add";
        }

        String fileUrl = mentorService.saveFile(mentorDTO.getFile(), uploadFolder);
        Integer seqId = mentorService.create(mentorDTO);
        System.out.println(seqId);
        Integer mentorId = mentorDTO.getSeqId();
        System.out.println(mentorId);
        MentorFileDTO mentorFileDTO = new MentorFileDTO(seqId,fileUrl,mentorId);
        Integer mentorFileId = mentorFileService.create(mentorFileDTO);
        mentorDTO.setMFId(mentorFileId);
        mentorService.update(seqId, mentorDTO);
        System.out.println(mentorDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("mentor.create.success"));
        return "redirect:/admin/mentors";
    }


    @GetMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Integer seqId, final Model model) {
        model.addAttribute("mentor", mentorService.get(seqId));
        return "admin/mentor/edit";
    }

    @PostMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Integer seqId,
                       @ModelAttribute("mentor") @Valid final MentorDTO mentorDTO,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/mentor/edit";
        }
        mentorService.update(seqId, mentorDTO);
        System.out.println();
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("mentor.update.success"));
        return "redirect:/admin/mentors";
    }

    @PostMapping("/delete/{seqId}")
    public String delete(@PathVariable(name = "seqId") final Integer seqId,
                         final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = mentorService.getReferencedWarning(seqId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            mentorService.delete(seqId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("mentor.delete.success"));
        }
        return "redirect:/admin/mentors";
    }


    @GetMapping("/status")
    public String mentorStatus(Model model, @AuthenticationPrincipal UserDetails sessionUser) {
        if (sessionUser == null) {
            return "redirect:/login"; // 인증되지 않은 사용자는 로그인 페이지로 리다이렉트
        }

        // 상태 값 전달
        model.addAttribute("userName", sessionUser);
        model.addAttribute("mentorOpenStatus", applicationState.isMentorOpenStatus()); // 현재 상태 값 전달
        return "admin/homeEdit/mentor-enroll-open";
    }


    @PostMapping("/status")
    public String updateFormOpenStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(name = "openStatus", defaultValue = "false") boolean openStatus,
            RedirectAttributes redirectAttributes,
            Model model) {
        // 세션 유저 확인
        if (userDetails == null) {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        // 로그 기록 등 처리 (데이터베이스 저장 로직 추가 가능)
        System.out.println("신청서 접수 오픈 상태: " + openStatus + ", 변경한 유저: " + userDetails.getUsername());
        applicationState.setMentorOpenStatus(openStatus);

        // 성공 메시지 전달
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("멘토 신청 접수를 "+openStatus+"로 변경하였습니다."));

        return "redirect:/admin/manage";
    }

}