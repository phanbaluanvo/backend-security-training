package kpu.cybersecurity.training.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kpu.cybersecurity.training.domain.dto.request.ReqLessonDTO;
import kpu.cybersecurity.training.domain.dto.response.ResLessonDTO;
import kpu.cybersecurity.training.domain.dto.response.ResModuleDTO;
import kpu.cybersecurity.training.domain.dto.response.ResultPaginationDTO;
import kpu.cybersecurity.training.domain.entity.Lesson;
import kpu.cybersecurity.training.domain.entity.LessonContent;
import kpu.cybersecurity.training.domain.entity.Module;
import kpu.cybersecurity.training.repository.LessonContentRepository;
import kpu.cybersecurity.training.repository.LessonRepository;
import kpu.cybersecurity.training.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private LessonContentRepository lessonContentRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private ModuleService moduleService;

    public void createLesson(ReqLessonDTO dto) {
        Module module = moduleService.getModuleByModuleId(dto.getModuleId());

        Lesson lesson = new Lesson();
        LessonContent content = new LessonContent();

        lesson.fromRequestDto(dto);
        lesson.setModule(module);

        content.fromRequestDto(dto);
        lesson.setContent(content);

        content.setLesson(lesson);

        lessonRepository.save(lesson);
    }

    public ResultPaginationDTO<ResLessonDTO> getPagingLesson(Specification<Lesson> spec, Pageable pageable) {
        Page<Lesson> pageLesson = lessonRepository.findAll(spec, pageable);

        pageLesson.map(
                lesson -> {
                    lesson.setContent(null);
                    return lesson;
                });

        ResultPaginationDTO<ResLessonDTO> res = new ResultPaginationDTO<>();
        res.setMetaAndResponse(pageLesson);

        return res;
    }

    public List<ResLessonDTO> getListLessons(Specification<Lesson> spec) {
        return lessonRepository.findAll(spec)
                .stream().map(Lesson::toResponseDto).toList();
    }

    public Lesson getLessonByLessonId(Long lessonId) {
        return this.lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson ID not found!"));
    }

    @Transactional
    public Lesson updateLesson(Long lessonId, ReqLessonDTO dto) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson ID not found"));

        boolean isChange = false;

        if(!dto.getLessonName().trim().equals(lesson.getLessonName())){
            isChange = true;
        }

        if(!dto.getModuleId().equals(lesson.getModule().getModuleId())) {
            lesson.setModule(this.moduleService.getModuleByModuleId(dto.getModuleId()));
            isChange = true;
        }

        if(dto.getContent() != null && !dto.getContent().isEmpty()) {
            LessonContent content = lesson.getContent();
            content.setContent(dto.getContent());
        }

        if(isChange) {
            lesson.fromRequestDto(dto);
            lessonRepository.save(lesson);
        }

        return lessonRepository.findById(lessonId).get();
    }

    @Transactional
    public void deleteLesson(Long lessonId) {
        this.lessonRepository.deleteById(lessonId);
    }

}



