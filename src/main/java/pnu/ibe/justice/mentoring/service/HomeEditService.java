package pnu.ibe.justice.mentoring.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pnu.ibe.justice.mentoring.domain.HomeEdit;
import pnu.ibe.justice.mentoring.model.HomeEditDTO;
import pnu.ibe.justice.mentoring.repos.HomeEditRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeEditService {

    private final HomeEditRepository homeEditRepository;

    public HomeEditService(HomeEditRepository homeEditRepository) {
        this.homeEditRepository = homeEditRepository;
    }

    public List<HomeEditDTO> findAll() {
        List<HomeEdit> homeEdits = homeEditRepository.findAll(Sort.by("seqId"));
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
}
