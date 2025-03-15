package kpu.cybersecurity.training.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kpu.cybersecurity.training.domain.dto.request.ReqModuleDTO;
import kpu.cybersecurity.training.domain.dto.response.ResModuleDTO;
import kpu.cybersecurity.training.domain.dto.response.ResultPaginationDTO;
import kpu.cybersecurity.training.domain.entity.Course;
import kpu.cybersecurity.training.domain.entity.Module;
import kpu.cybersecurity.training.domain.entity.Topic;
import kpu.cybersecurity.training.repository.LessonRepository;
import kpu.cybersecurity.training.repository.ModuleRepository;
import kpu.cybersecurity.training.util.exception.UniqueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseService courseService;

    public void createModule(ReqModuleDTO dto) throws UniqueException {
        checkExistsUniqueName(dto.getModuleName().trim());

        Course course = courseService.getCourseByCourseId(dto.getCourseId());

        Module module = new Module();
        module.fromRequestDto(dto);
        module.setCourse(course);

        moduleRepository.save(module);
    }

    public Module getModuleByModuleId(Long moduleId) {
        return moduleRepository.findModuleByModuleId(moduleId)
                .orElseThrow(() -> new EntityNotFoundException("Module ID not found"));
    }

    public ResultPaginationDTO<ResModuleDTO> getPagingModules(Specification<Module> spec, Pageable pageable) {
        Page<Module> pageModule = moduleRepository.findAll(spec, pageable);

        ResultPaginationDTO<ResModuleDTO> res = new ResultPaginationDTO<>();
        res.setMetaAndResponse(pageModule);

        return res;
    }

    public List<ResModuleDTO> getListModules(Specification<Module> spec) {
        return moduleRepository.findAll(spec)
                .stream().map(Module::toResponseDto).toList();
    }

    @Transactional
    public Module updateModule(Long moduleId, ReqModuleDTO dto) throws UniqueException {
        Module module = moduleRepository.findModuleByModuleId(moduleId)
                .orElseThrow(() -> new EntityNotFoundException("Module ID not found"));

        boolean isChange = false;

        if(!dto.getModuleName().trim().equals(module.getModuleName())) {
            checkExistsUniqueName(dto.getModuleName().trim());
            isChange = true;
        }

        if(!module.getCourse().getCourseId().equals(dto.getCourseId())) {
            var course = this.courseService.getCourseByCourseId(dto.getCourseId());
            module.setCourse(course);
            isChange = true;
        }

        if(!dto.getDescription().equals(module.getDescription())) {
            isChange = true;
        }

        if(isChange) {
            module.fromRequestDto(dto);
            moduleRepository.save(module);
        }

        return moduleRepository.findModuleByModuleId(moduleId).get();
    }


    @Transactional
    public void deleteModule(Long moduleId) {
        this.lessonRepository.assignNewModuleToLesson(moduleId, null, null);
        this.moduleRepository.deleteById(moduleId);
    }

    public void checkExistsUniqueName(String name) throws UniqueException {
        if(moduleRepository.existsModuleByModuleName(name)) throw new UniqueException("Module name already exists");
    }

    public void assignNewCourseToModule(Long newTopicId, List<Long> moduleIds) {
        this.moduleRepository.assignNewCourseToModule(null, newTopicId, moduleIds);
    }
}
