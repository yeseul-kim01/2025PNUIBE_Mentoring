package pnu.ibe.justice.mentoring.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.ibe.justice.mentoring.domain.Notice;
import pnu.ibe.justice.mentoring.domain.NoticeFile;

import java.util.List;


public interface NoticeFileRepository extends JpaRepository<NoticeFile, Integer> {

    NoticeFile findFirstByNotice(Notice notice);

    List<NoticeFile> findNoticeFileByNotice_SeqId(Integer noticeId);

}
