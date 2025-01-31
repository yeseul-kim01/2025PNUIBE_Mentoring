package pnu.ibe.justice.mentoring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnu.ibe.justice.mentoring.domain.HomeEdit;
import pnu.ibe.justice.mentoring.domain.Mentor;
import pnu.ibe.justice.mentoring.domain.User;
import pnu.ibe.justice.mentoring.model.HomeEditDTO;
import pnu.ibe.justice.mentoring.model.MentorDTO;
import pnu.ibe.justice.mentoring.repos.HomeEditRepository;
import pnu.ibe.justice.mentoring.util.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeEditService {

    private final HomeEditRepository homeEditRepository;

    @Cacheable(value = "homeEditsCache", key = "'allHomeEdits'")
    public List<HomeEditDTO> findAll() {
        List<HomeEdit> homeEdits = homeEditRepository.findAll(Sort.by("seqId"));
        System.out.println("No Cache: HomeEditService.findAll()"+homeEdits.toString());
        return homeEdits.stream()
                .map(homeEdit -> mapToDTO(homeEdit,new HomeEditDTO()))
                .collect(Collectors.toList());
    }

    public HomeEditDTO mapToDTO(final HomeEdit homeEdit , final HomeEditDTO homeEditDTO) {
        homeEditDTO.setSeqId(homeEdit.getSeqId());
        homeEditDTO.setContentType(homeEdit.getContentType());
        homeEditDTO.setMainHome_content(homeEdit.getMainHome_content());
        return homeEditDTO;
    }


    private HomeEdit mapToEntity(final HomeEditDTO homeEditDTO, final HomeEdit homeEdit) {
        // Check if each field is not null before setting the value
        if (homeEditDTO.getContentType() != null) {
            homeEdit.setContentType(homeEditDTO.getContentType());
        }
        if (homeEditDTO.getMainHome_content() != null) {
            homeEdit.setMainHome_content(homeEditDTO.getMainHome_content());
        }
        return homeEdit;
    }


    public Object get(Integer seqId) {
        return homeEditRepository.findById(seqId)
                .map(homeEdit -> mapToDTO(homeEdit, new HomeEditDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void update(Integer seqId, HomeEditDTO homeEditDTO) {
        final HomeEdit homeEdit = homeEditRepository.findById(seqId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(homeEditDTO, homeEdit);
        homeEditRepository.save(homeEdit);
    }
}
