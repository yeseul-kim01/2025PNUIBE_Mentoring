package pnu.ibe.justice.mentoring.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.ibe.justice.mentoring.domain.Question;
import pnu.ibe.justice.mentoring.domain.User;

import java.util.Optional;


public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Question findFirstByUsers(User user);
    Optional<Question> findBymFId(int mFId);
}