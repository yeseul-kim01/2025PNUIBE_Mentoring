package pnu.ibe.justice.mentoring.controller.admin;

import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;
import pnu.ibe.justice.mentoring.domain.MentorFile;
import pnu.ibe.justice.mentoring.domain.User;
import pnu.ibe.justice.mentoring.domain.UserFile;
import pnu.ibe.justice.mentoring.model.UserFileDTO;
import pnu.ibe.justice.mentoring.repos.UserRepository;
import pnu.ibe.justice.mentoring.service.ApplicationState;
import pnu.ibe.justice.mentoring.service.UserFileService;
import pnu.ibe.justice.mentoring.util.CustomCollectors;
import pnu.ibe.justice.mentoring.util.WebUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Controller
@RequestMapping("/admin/userFiles")
public class UserFileController {

    private final UserFileService userFileService;
    private final UserRepository userRepository;
    private final ApplicationState applicationState;
    private String uploadFolder = "/Users/gim-yeseul/Desktop/mentoring_pj/mentoring/upload/";

    public UserFileController(final UserFileService userFileService,
            final UserRepository userRepository, final ApplicationState applicationState) {
        this.userFileService = userFileService;
        this.userRepository = userRepository;
        this.applicationState = applicationState;
    }


    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userValues", userRepository.findAll(Sort.by("seqId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getSeqId, User::getEmail)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("userFiles", userFileService.findAll());
        return "admin/userFile/list";
    }

    //멘티폼 다운로드하기.

    @GetMapping("/{mFId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer mFId, @RequestHeader(value = "Hx-Request", required = false) String hxRequestHeader) throws MalformedURLException {


        UserFile userFile = userFileService.findFileById(mFId);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy");
        String formattedDate = userFile.getDateCreated().format(outputFormatter);
        String userFileName = userFile.getFileSrc();
        String MenteeFileApplication = "menteeApplication";
        File file = new File(uploadFolder + "/"+ formattedDate + "/" + MenteeFileApplication +"/" + userFileName);
        UrlResource urlResource = new UrlResource(file.toURI());
        String encodedUploadFileName = UriUtils.encode(userFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment;  filename=\""  + encodedUploadFileName + "\"";
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("userFile") final UserFileDTO userFileDTO,
                      Model model) {
        model.addAttribute("menteeOpenStatus", applicationState.isMenteeOpenStatus()); // 현재 상태 값 전달
        return "admin/userFile/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("userFile") @Valid final UserFileDTO userFileDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/userFile/add";
        }
        userFileService.create(userFileDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("userFile.create.success"));
        return "redirect:/admin/userFiles";
    }




    @GetMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Integer seqId, final Model model) {
        model.addAttribute("userFile", userFileService.get(seqId));
        return "admin/userFile/edit";
    }

    @PostMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Integer seqId,
            @ModelAttribute("userFile") @Valid final UserFileDTO userFileDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/userFile/edit";
        }
        userFileService.update(seqId, userFileDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("userFile.update.success"));
        return "redirect:/admin/userFiles";
    }

    @PostMapping("/delete/{seqId}")
    public String delete(@PathVariable(name = "seqId") final Integer seqId,
            final RedirectAttributes redirectAttributes) {
        userFileService.delete(seqId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("userFile.delete.success"));
        return "redirect:/admin/userFiles";
    }



    @GetMapping("/status")
    public String mentorStatus(Model model, @AuthenticationPrincipal UserDetails sessionUser) {
        if (sessionUser == null) {
            return "redirect:/login"; // 인증되지 않은 사용자는 로그인 페이지로 리다이렉트
        }

        // 상태 값 전달
        model.addAttribute("userName", sessionUser);
        model.addAttribute("menteeOpenStatus", applicationState.isMenteeOpenStatus()); // 현재 상태 값 전달
        return "admin/homeEdit/mentee-enroll-open";
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
        applicationState.setMenteeOpenStatus(openStatus);

        // 성공 메시지 전달
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("멘티 신청 접수를 "+openStatus+"로 변경하였습니다."));

        return "redirect:/admin/manage";
    }


}
