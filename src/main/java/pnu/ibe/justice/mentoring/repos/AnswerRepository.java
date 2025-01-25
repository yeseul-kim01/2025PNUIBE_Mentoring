package pnu.ibe.justice.mentoring.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pnu.ibe.justice.mentoring.domain.Answer;
import pnu.ibe.justice.mentoring.domain.Question;
import pnu.ibe.justice.mentoring.domain.User;
import pnu.ibe.justice.mentoring.model.AnswerDTO;

import java.util.List;
import java.util.Optional;


public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findByQuestion(Question question);
    Answer findFirstByQuestion(Question question);
    Answer findFirstByUsers(User user);
    List<Answer> findByqRId(Integer id);
//    @Query("SELECT a FROM Answer a JOIN FETCH a.users WHERE a.qRId = :id")
//    List<Answer> findAnswersWithUser(@Param("id") Integer id);
    @Query("SELECT a FROM Answer a " +
            "JOIN FETCH a.users u " +
            "WHERE a.qRId = :qRId")
    List<Answer> findAnswersWithUsersByQRId(@Param("qRId") Integer qRId);
}