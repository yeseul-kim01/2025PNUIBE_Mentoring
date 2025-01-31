package pnu.ibe.justice.mentoring.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.ibe.justice.mentoring.domain.Manager;

public interface ManagerRepository  extends JpaRepository<Manager, Integer> {

    Manager findManagerBySeqId(Integer seqId);
}
