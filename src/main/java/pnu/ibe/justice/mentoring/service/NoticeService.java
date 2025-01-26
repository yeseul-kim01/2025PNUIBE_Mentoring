package pnu.ibe.justice.mentoring.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pnu.ibe.justice.mentoring.domain.Mentor;
import pnu.ibe.justice.mentoring.domain.Notice;
import pnu.ibe.justice.mentoring.domain.NoticeFile;
import pnu.ibe.justice.mentoring.domain.User;
import pnu.ibe.justice.mentoring.model.MentorDTO;
import pnu.ibe.justice.mentoring.model.NoticeDTO;
import pnu.ibe.justice.mentoring.model.NoticeFileDTO;
import pnu.ibe.justice.mentoring.repos.NoticeFileRepository;
import pnu.ibe.justice.mentoring.repos.NoticeRepository;
import pnu.ibe.justice.mentoring.repos.UserRepository;
import pnu.ibe.justice.mentoring.util.NotFoundException;
import pnu.ibe.justice.mentoring.util.ReferencedWarning;
import org.springframework.beans.factory.annotation.Value;


@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final NoticeFileRepository noticeFileRepository;
    private final NoticeFileService noticeFileService;
    String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
    @Value("${file.upload-folder}")
    private String uploadFolder;

    public NoticeService(final NoticeRepository noticeRepository,
                         final UserRepository userRepository, final NoticeFileRepository noticeFileRepository,
                         final NoticeFileService noticeFileService) {
        this.noticeRepository = noticeRepository;
        this.userRepository = userRepository;
        this.noticeFileRepository = noticeFileRepository;
        this.noticeFileService = noticeFileService;
    }

    public List<NoticeDTO> findNoticeByIsmust(Boolean ismust) {
        // 카테고리가 주어진 값과 일치하는 멘토를 찾음
        List<Notice> notices = noticeRepository.findByIsMust(ismust);

        // notice 엔티티를 noticeDTO 변환
        return notices.stream()
                .map(notice -> mapToDTO(notice, new NoticeDTO()))
                .collect(Collectors.toList());
    }


    public List<NoticeDTO> findAll() {
        final List<Notice> notices = noticeRepository.findAll(Sort.by("seqId"));
        return notices.stream()
                .map(notice -> mapToDTO(notice, new NoticeDTO()))
                .toList();
    }


    public List<Map<String, Object>> saveFiles(MultipartFile[] files, String uploadFolder) {
        String notice = "Notice/";
//        String userseqId = subCategoryName;
        Path folderPath = Paths.get(uploadFolder +"/"+dateFolder+"/"+ notice);
        List<Map<String, Object>> fileInfoList = new ArrayList<>();

        try {
            // 폴더 생성
            Files.createDirectories(folderPath);

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue; // 비어 있는 파일은 무시
                }

                Map<String, Object> originNameMap = new HashMap<>();
                String originName = file.getOriginalFilename();
                UUID uuid = uploadFileNameMake(); // UUID 생성 메서드 호출

                originNameMap.put("origin", originName); // 문자열에서 UUID 생성
                originNameMap.put("uuid", uuid);

                String filesrc = uuid + "_" + originName;
                //String fileUrl = uploadFolder + dateFolder + "/" + item + filesrc;
                String fileUrl =dateFolder + "/" + notice + filesrc;
                // 파일 저장 경로
                Path filePath = folderPath.resolve(filesrc);
                originNameMap.put("src",fileUrl);
                file.transferTo(filePath.toFile()); // 파일 저장
                System.out.println("File saved at: " + fileUrl);

                fileInfoList.add(originNameMap); // 각 파일의 정보를 리스트에 추가
            }
        } catch (Exception e) {
            System.out.println("Error while saving files: " + e.getMessage());
        }

        return fileInfoList;
    }

    //file name 중복 저장 방지를 위한 난수 생성
    private UUID uploadFileNameMake() {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }
//
//    //file 한개 일 때
//    public String saveFile(MultipartFile multipartFile, String uploadFolder){
//        String fileUrl="";
//        String noticefolder = "notice/";
//        Path folderPath = Paths.get(uploadFolder + dateFolder + "/" + noticefolder);
//
//        try {
//            Files.createDirectories(folderPath);
//            Path filePath = folderPath.resolve(multipartFile.getOriginalFilename());
//            multipartFile.transferTo(filePath.toFile());
//            fileUrl = "/Users/gim-yeseul/Desktop/mentoring_pj/mentoring/upload/" + dateFolder + "/" + noticefolder + multipartFile.getOriginalFilename();
//            System.out.println("File saved at: " + fileUrl);
//            System.out.println(dateFolder);
//        }catch(Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return multipartFile.getOriginalFilename();
//    }

    public NoticeDTO get(final Integer seqId) {
        return noticeRepository.findById(seqId)
                .map(notice -> mapToDTO(notice, new NoticeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void createNotice(final NoticeDTO noticeDTO) {
        Integer noticeId = create(noticeDTO);

        List<Map<String, Object>> fileUrl = null;
        if (noticeDTO.getFile() != null) {
            MultipartFile[] files = noticeDTO.getFile();
            fileUrl = saveFiles(files, uploadFolder);
        }

        for (Map<String, Object> fileInfo : fileUrl) {
            NoticeFileDTO noticeFileDTO = new NoticeFileDTO();
            noticeFileDTO.setFilename((String) fileInfo.get("origin")); // 원본 이름 UUID
            noticeFileDTO.setUuid((UUID) fileInfo.get("uuid")); // UUID
            noticeFileDTO.setNotice(noticeId); // 연결된 Item의 ID
            noticeFileDTO.setFileSrc((String) fileInfo.get("src"));
            // 이미지 정보 저장
            noticeFileService.create(noticeFileDTO);
        }

    }

    public Integer create(final NoticeDTO noticeDTO) {
        final Notice notice = new Notice();
        OffsetDateTime currentDateTime = OffsetDateTime.now();
        notice.setDateCreated(currentDateTime);
        notice.setLastUpdated(currentDateTime);
        mapToEntity(noticeDTO, notice);
        return noticeRepository.save(notice).getSeqId();
    }

    public void update(final Integer seqId, final NoticeDTO noticeDTO) {
        final Notice notice = noticeRepository.findById(seqId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(noticeDTO, notice);
        noticeRepository.save(notice);
    }

    public void delete(final Integer seqId) {
        noticeRepository.deleteById(seqId);
    }

    private NoticeDTO mapToDTO(final Notice notice, final NoticeDTO noticeDTO) {
        noticeDTO.setSeqId(notice.getSeqId());
        noticeDTO.setTitle(notice.getTitle());
        noticeDTO.setContent(notice.getContent());
        noticeDTO.setIsPopup(notice.getIsPopup());
        noticeDTO.setIsMust(notice.getIsMust());
        noticeDTO.setUsers(notice.getUsers() == null ? null : notice.getUsers());
        return noticeDTO;
    }

    private Notice mapToEntity(final NoticeDTO noticeDTO, final Notice notice) {
        notice.setTitle(noticeDTO.getTitle());
        notice.setContent(noticeDTO.getContent());
        notice.setIsPopup(noticeDTO.getIsPopup());
        notice.setIsMust(noticeDTO.getIsMust());
        final User users = noticeDTO.getUsers() == null ? null : userRepository.findById(noticeDTO.getUsers().getSeqId())
                .orElseThrow(() -> new NotFoundException("users not found"));
        notice.setUsers(users);
        return notice;
    }

    public ReferencedWarning getReferencedWarning(final Integer seqId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Notice notice = noticeRepository.findById(seqId)
                .orElseThrow(NotFoundException::new);
        final NoticeFile noticeNoticeFile = noticeFileRepository.findFirstByNotice(notice);
        if (noticeNoticeFile != null) {
            referencedWarning.setKey("notice.noticeFile.notice.referenced");
            referencedWarning.addParam(noticeNoticeFile.getSeqId());
            return referencedWarning;
        }
        return null;
    }

    public Page<Notice> getList(int page) {
        Pageable pageable = PageRequest.of(page, 6, Sort.by(Sort.Direction.DESC, "dateCreated"));
        return this.noticeRepository.findAll(pageable);
    }

}
