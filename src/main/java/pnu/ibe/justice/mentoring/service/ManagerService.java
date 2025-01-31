package pnu.ibe.justice.mentoring.service;

import org.springframework.stereotype.Service;
import pnu.ibe.justice.mentoring.domain.Manager;
import pnu.ibe.justice.mentoring.domain.Notice;
import pnu.ibe.justice.mentoring.domain.UserFile;
import pnu.ibe.justice.mentoring.model.ManagerDTO;
import pnu.ibe.justice.mentoring.model.NoticeDTO;
import pnu.ibe.justice.mentoring.model.UserFileDTO;
import pnu.ibe.justice.mentoring.repos.ManagerRepository;
import pnu.ibe.justice.mentoring.util.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository){
        this.managerRepository = managerRepository;
    }
    public List<ManagerDTO> findAll() {
        final List<Manager> managers = managerRepository.findAll();

        return managers.stream()
                .map(manager -> mapToDTO(manager, new ManagerDTO()))
                .collect(Collectors.toList());
    }

    public Integer create(final ManagerDTO managerDTO) {
        final Manager manager = new Manager();
        mapToEntity(managerDTO, manager);
        return managerRepository.save(manager).getSeqId();
    }


    public ManagerDTO get(Integer seqId) {
        Manager manager = managerRepository.findManagerBySeqId(seqId);
        ManagerDTO managerDTO = new ManagerDTO();
        mapToDTO(manager,managerDTO);
        return managerDTO;
    }

    public void update(Integer seqId, ManagerDTO managerDTO) {
        final Manager manager = managerRepository.findManagerBySeqId(seqId);
        mapToEntity(managerDTO,manager);
        managerRepository.save(manager);


    }

    private Manager mapToEntity(final ManagerDTO managerDTO, final Manager manager) {
        manager.setName(managerDTO.getName());
        manager.setEmail(managerDTO.getEmail());
        manager.setContent(managerDTO.getContent());
        manager.setGrade(managerDTO.getGrade());
        return manager;
    }

    private ManagerDTO mapToDTO(final Manager manager,final ManagerDTO managerDTO) {
        managerDTO.setName(manager.getName());
        managerDTO.setEmail(manager.getEmail());
        managerDTO.setContent(manager.getContent());
        managerDTO.setGrade(manager.getGrade());
        managerDTO.setSeqId(manager.getSeqId());

        return managerDTO;
    }

    public void delete(Integer seqId) {
        managerRepository.deleteById(seqId);
    }
}
