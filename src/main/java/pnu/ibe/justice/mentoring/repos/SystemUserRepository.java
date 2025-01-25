package pnu.ibe.justice.mentoring.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.ibe.justice.mentoring.domain.SystemUser;
import pnu.ibe.justice.mentoring.domain.User;

import java.util.Optional;

public interface SystemUserRepository extends JpaRepository<SystemUser, Integer> {
    Optional<SystemUser> findById(Integer Id);
    Optional<SystemUser> findByName(String username);
}
