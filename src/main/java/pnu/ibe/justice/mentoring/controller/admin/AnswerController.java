package pnu.ibe.justice.mentoring.controller.admin;

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
import pnu.ibe.justice.mentoring.domain.Question;
import pnu.ibe.justice.mentoring.domain.User;
import pnu.ibe.justice.mentoring.model.AnswerDTO;
import pnu.ibe.justice.mentoring.repos.QuestionRepository;
import pnu.ibe.justice.mentoring.repos.UserRepository;
import pnu.ibe.justice.mentoring.service.AnswerService;
import pnu.ibe.justice.mentoring.service.HomeEditService;
import pnu.ibe.justice.mentoring.util.CustomCollectors;
import pnu.ibe.justice.mentoring.util.ReferencedWarning;
import pnu.ibe.justice.mentoring.util.WebUtils;


@Controller
@RequestMapping("/admin/answers")
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final HomeEditService homeEditService;

    public AnswerController(final AnswerService answerService,
            final QuestionRepository questionRepository, final UserRepository userRepository,
                            final HomeEditService homeEditService) {
        this.answerService = answerService;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.homeEditService = homeEditService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("questionValues", questionRepository.findAll(Sort.by("seqId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Question::getSeqId, Question::getTitle)));
        model.addAttribute("usersValues", userRepository.findAll(Sort.by("seqId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getSeqId, User::getEmail)));
        model.addAttribute("homeEdits",homeEditService.findAll());
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("answers", answerService.findAll());
        return "admin/answer/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("answer") final AnswerDTO answerDTO) {
        return "admin/answer/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("answer") @Valid final AnswerDTO answerDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/answer/add";
        }
        answerService.create(answerDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("answer.create.success"));
        return "redirect:/admin/answers";
    }

    @GetMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Integer seqId, final Model model) {
        model.addAttribute("answer", answerService.get(seqId));
        return "admin/answer/edit";
    }

    @PostMapping("/edit/{seqId}")
    public String edit(@PathVariable(name = "seqId") final Integer seqId,
            @ModelAttribute("answer") @Valid final AnswerDTO answerDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/answer/edit";
        }
        answerService.update(seqId, answerDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("answer.update.success"));
        return "redirect:/admin/answers";
    }

    @PostMapping("/delete/{seqId}")
    public String delete(@PathVariable(name = "seqId") final Integer seqId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = answerService.getReferencedWarning(seqId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            answerService.delete(seqId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("answer.delete.success"));
        }
        return "redirect:/admin/answers";
    }

}
