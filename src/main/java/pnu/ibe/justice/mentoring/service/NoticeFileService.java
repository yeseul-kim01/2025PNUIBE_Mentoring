package pnu.ibe.justice.mentoring.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnu.ibe.justice.mentoring.domain.MentorFile;
import pnu.ibe.justice.mentoring.domain.Notice;
import pnu.ibe.justice.mentoring.domain.NoticeFile;
import pnu.ibe.justice.mentoring.model.NoticeFileDTO;
import pnu.ibe.justice.mentoring.repos.NoticeFileRepository;
import pnu.ibe.justice.mentoring.repos.NoticeRepository;
import pnu.ibe.justice.mentoring.util.NotFoundException;

import org.springframework.beans.factory.annotation.Value;



@Service
public class NoticeFileService {

    private final NoticeFileRepository noticeFileRepository;
    private final NoticeRepository noticeRepository;

    @Value("${file.upload-folder}")
    private String uploadFolder;


    public NoticeFileService(final NoticeFileRepository noticeFileRepository,
            final NoticeRepository noticeRepository) {
        this.noticeFileRepository = noticeFileRepository;
        this.noticeRepository = noticeRepository;
    }

    public List<NoticeFileDTO> findAll() {
        final List<NoticeFile> noticeFiles = noticeFileRepository.findAll(Sort.by("seqId"));
        return noticeFiles.stream()
                .map(noticeFile -> mapToDTO(noticeFile, new NoticeFileDTO()))
                .toList();
    }

    public NoticeFile findFileById(final Integer id) {
        Optional<NoticeFile> OPnoticeFile = noticeFileRepository.findById(id);
        NoticeFile noticeFile = null;
        if (OPnoticeFile.isPresent()) {
            noticeFile = OPnoticeFile.get();
        } else {
            System.out.println("error");
        }
        return noticeFile;
    }

    @Transactional
    public List<NoticeFileDTO> findFileByseqId(final Integer id) {
        List<NoticeFile> noticeFiles = noticeFileRepository.findNoticeFileByNotice_SeqId(id);
        if (!noticeFiles.isEmpty()) {
            List<NoticeFileDTO> noticefiles = new ArrayList<>();
            for (NoticeFile noticefile : noticeFiles) {
                noticefiles.add(mapToDTO(noticefile, new NoticeFileDTO()));
            }
            return noticefiles;
        } else {
            System.out.println("error");
            return null;
        }
    }

    @Transactional
    public File createZipFile(String zipName,Integer seqId, List<NoticeFileDTO> noticeFiles) throws IOException {
        String zipFileName = zipName +"_"+ seqId + ".zip";
        File zipFile = new File(uploadFolder + zipFileName);

        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            for (NoticeFileDTO noticeFile : noticeFiles) {
                String noticeFileName = noticeFile.getFileSrc();
                File file = new File(uploadFolder  + "/" + noticeFileName);

                if (file.exists()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        ZipEntry zipEntry = new ZipEntry(noticeFile.getFilename());
                        zipOut.putNextEntry(zipEntry);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) >= 0) {
                            zipOut.write(buffer, 0, length);
                        }
                    }
                }
            }
        }
        return zipFile;
    }

    public NoticeFileDTO get(final Integer seqId) {
        return noticeFileRepository.findById(seqId)
                .map(noticeFile -> mapToDTO(noticeFile, new NoticeFileDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final NoticeFileDTO noticeFileDTO) {
        final NoticeFile noticeFile = new NoticeFile();
        mapToEntity(noticeFileDTO, noticeFile);
        return noticeFileRepository.save(noticeFile).getSeqId();
    }

    public void update(final Integer seqId, final NoticeFileDTO noticeFileDTO) {
        final NoticeFile noticeFile = noticeFileRepository.findById(seqId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(noticeFileDTO, noticeFile);
        noticeFileRepository.save(noticeFile);
    }

    public void delete(final Integer seqId) {
        noticeFileRepository.deleteById(seqId);
    }

    private NoticeFileDTO mapToDTO(final NoticeFile noticeFile, final NoticeFileDTO noticeFileDTO) {
        noticeFileDTO.setSeqId(noticeFile.getSeqId());
        noticeFileDTO.setFileSrc(noticeFile.getFileSrc());
//        noticeFileDTO.setType(noticeFile.getType());
        noticeFileDTO.setFilename(noticeFile.getFilename());
        noticeFileDTO.setUserSeqId(noticeFile.getUserSeqId());
        noticeFileDTO.setNotice(noticeFile.getNotice() == null ? null : noticeFile.getNotice().getSeqId());
        return noticeFileDTO;
    }

    private NoticeFile mapToEntity(final NoticeFileDTO noticeFileDTO, final NoticeFile noticeFile) {
        noticeFile.setFileSrc(noticeFileDTO.getFileSrc());
        noticeFile.setFilename(noticeFileDTO.getFilename());
        final Notice notice = noticeFileDTO.getNotice() == null ? null : noticeRepository.findById(noticeFileDTO.getNotice())
                .orElseThrow(() -> new NotFoundException("notice not found"));
        noticeFile.setNotice(notice);
        return noticeFile;
    }

}
