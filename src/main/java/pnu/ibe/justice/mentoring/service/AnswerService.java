package pnu.ibe.justice.mentoring.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pnu.ibe.justice.mentoring.domain.*;
import pnu.ibe.justice.mentoring.model.AnswerDTO;
import pnu.ibe.justice.mentoring.model.AnswerUserDTO;
import pnu.ibe.justice.mentoring.model.SubmitAnswerDTO;
import pnu.ibe.justice.mentoring.repos.AnswerFileRepository;
import pnu.ibe.justice.mentoring.repos.AnswerRepository;
import pnu.ibe.justice.mentoring.repos.QuestionRepository;
import pnu.ibe.justice.mentoring.repos.UserRepository;
import pnu.ibe.justice.mentoring.util.NotFoundException;
import pnu.ibe.justice.mentoring.util.ReferencedWarning;


@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerFileRepository answerFileRepository;

    public AnswerService(final AnswerRepository answerRepository,
                         final QuestionRepository questionRepository, final UserRepository userRepository,
                         final AnswerFileRepository answerFileRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.answerFileRepository = answerFileRepository;
    }

    public List<AnswerDTO> findAll() {
        final List<Answer> answers = answerRepository.findAll(Sort.by("seqId"));
        return answers.stream()
                .map(answer -> mapToDTO(answer, new AnswerDTO()))
                .toList();
    }

    public List<Answer> getAnswersForQuestion(Integer questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        return answerRepository.findByQuestion(question);
    }

    public AnswerDTO get(final Integer seqId) {
        return answerRepository.findById(seqId)
                .map(answer -> mapToDTO(answer, new AnswerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public List<AnswerDTO> findByQRId(int QRId) {
        final List<Answer> answers = answerRepository.findByqRId(QRId);
        return answers.stream()
                .map(Answer -> mapToDTO(Answer, new AnswerDTO()))
                .toList();
    }

    public Integer create(final AnswerDTO answerDTO) {
        final Answer answer = new Answer();
        mapToEntity(answerDTO, answer);
        return answerRepository.save(answer).getSeqId();
    }

    public void update(final Integer seqId, final AnswerDTO answerDTO) {
        final Answer answer = answerRepository.findById(seqId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(answerDTO, answer);
        answerRepository.save(answer);
    }

    public void delete(final Integer seqId) {
        answerRepository.deleteById(seqId);
    }

    private AnswerDTO mapToDTO(final Answer answer, final AnswerDTO answerDTO) {
        answerDTO.setSeqId(answer.getSeqId());
        answerDTO.setContent(answer.getContent());
        answerDTO.setQuestion(answer.getQuestion() == null
                ? null : answer.getQuestion());
        answerDTO.setUsers(answer.getUsers() == null ? null : answer.getUsers());
        answerDTO.setQRId(answer.getQuestion().getSeqId());
        return answerDTO;
    }

    private Answer mapToEntity(final AnswerDTO answerDTO, final Answer answer) {
        answer.setContent(answerDTO.getContent());
        final Question question = answerDTO.getQuestion() == null ? null : questionRepository.findById(answerDTO.getQuestion().getSeqId())
                .orElseThrow(() -> new NotFoundException("question not found"));
        answer.setQuestion(question);
        final User users = answerDTO.getUsers() == null ? null : userRepository.findById(answerDTO.getUsers().getSeqId())
                .orElseThrow(() -> new NotFoundException("users not found"));
        answer.setUsers(users);
        answer.setQRId(question.getSeqId());
        return answer;
    }

    public ReferencedWarning getReferencedWarning(final Integer seqId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Answer answer = answerRepository.findById(seqId)
                .orElseThrow(NotFoundException::new);
        final AnswerFile answerAnswerFile = answerFileRepository.findFirstByAnswer(answer);
        if (answerAnswerFile != null) {
            referencedWarning.setKey("answer.answerFile.answer.referenced");
            referencedWarning.addParam(answerAnswerFile.getSeqId());
            return referencedWarning;
        }
        return null;
    }

    public List<AnswerUserDTO> getAnswerDetailsByQRId(Integer qRId) {
        List<Answer> answers = answerRepository.findAnswersWithUsersByQRId(qRId);

        return answers.stream()
                .map(answer -> {
                    AnswerUserDTO dto = new AnswerUserDTO();
                    dto.setContent(answer.getContent());

                    // 필요한 유저 데이터만 설정
                    if (answer.getUsers() != null) {
                        dto.setName(answer.getUsers().getName());
                        dto.setRole(answer.getUsers().getRole());
                    }

                    dto.setQRId(answer.getQRId());
                    return dto;
                })
                .toList();
    }

}