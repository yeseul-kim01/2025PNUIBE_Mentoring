package pnu.ibe.justice.mentoring.controller.admin;

import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;
import pnu.ibe.justice.mentoring.domain.MentorFile;
import pnu.ibe.justice.mentoring.domain.Notice;
import pnu.ibe.justice.mentoring.domain.NoticeFile;
import pnu.ibe.justice.mentoring.model.NoticeFileDTO;
import pnu.ibe.justice.mentoring.repos.NoticeRepository;
import pnu.ibe.justice.mentoring.service.NoticeFileService;
import pnu.ibe.justice.mentoring.service.NoticeService;
import pnu.ibe.justice.mentoring.util.CustomCollectors;
import pnu.ibe.justice.mentoring.util.WebUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequestMapping("/admin/noticeFiles")
public class NoticeFileController {

    private final NoticeFileService noticeFileService;
    private final NoticeRepository noticeRepository;
    private String uploadFolder = "/Users/gim-yeseul/Desktop/mentoring_pj/mentoring/upload/";

    public NoticeFileController(final NoticeFileService noticeFileService,
                                final NoticeRepository noticeRepository, NoticeService noticeService) {
        this.noticeFileService = noticeFileService;
        this.noticeRepository = noticeRepository;
        this.noticeService = noticeService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("noticeValues", noticeRepository.findAll(Sort.by("seqId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Notice::getSeqId, Notice::getTitle)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("noticeFiles", noticeFileService.findAll());
        return "admin/noticeFile/list";
    }

    @GetMapping("/{seqId}/download")
    public ResponseEntity<StreamingResponseBody> downloadFile(@PathVariable Integer seqId, @RequestHeader(value = "Hx-Request", required = false) String hxRequestHeader) throws IOException {


        List<NoticeFileDTO> noticeFiles = noticeFileService.findFileByseqId(seqId);
        String fileName = noticeService.get(seqId).getTitle();

        File zipFile = noticeFileService.createZipFile(fileName,seqId, noticeFiles);
            // StreamingResponseBody로 파일 스트림 반환
        StreamingResponseBody stream = outputStream -> {
            try (FileInputStream fis = new FileInputStream(zipFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            } finally {
                // 파일 삭제
                Files.deleteIfExists(zipFile.toPath());
                System.out.println("Temporary ZIP file deleted: " + zipFile.getName());
            }
        };

        // HTTP 응답 설정
        String encodedFileName = UriUtils.encode(zipFile.getName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(stream);
    }


    @GetMapping("/add")
    public String add(@ModelAttribute("noticeFile") final NoticeFileDTO noticeFileDTO) {
        return "admin/noticeFile/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("noticeFile") @Valid final NoticeFileDTO noticeFileDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/noticeFile/add";
        }
        noticeFileService.create(noticeFileDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("noticeFile.create.success"));
        return "redirect:/admin/noticeFiles";
    }

    @GetMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Integer seqId, final Model model) {
        model.addAttribute("noticeFile", noticeFileService.get(seqId));
        return "admin/noticeFile/edit";
    }

    @PostMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Integer seqId,
            @ModelAttribute("noticeFile") @Valid final NoticeFileDTO noticeFileDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/noticeFile/edit";
        }
        noticeFileService.update(seqId, noticeFileDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("noticeFile.update.success"));
        return "redirect:/admin/noticeFiles";
    }

    @PostMapping("/delete/{seqId}")
    public String delete(@PathVariable(name = "seqId") final Integer seqId,
            final RedirectAttributes redirectAttributes) {
        noticeFileService.delete(seqId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("noticeFile.delete.success"));
        return "redirect:/admin/noticeFiles";
    }

    private final NoticeService noticeService;
}
