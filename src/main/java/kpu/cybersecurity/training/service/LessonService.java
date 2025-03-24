package kpu.cybersecurity.training.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kpu.cybersecurity.training.domain.dto.request.ReqLessonDTO;
import kpu.cybersecurity.training.domain.dto.response.ResLessonDTO;
import kpu.cybersecurity.training.domain.dto.response.ResModuleDTO;
import kpu.cybersecurity.training.domain.dto.response.ResultPaginationDTO;
import kpu.cybersecurity.training.domain.entity.*;
import kpu.cybersecurity.training.domain.entity.Module;
import kpu.cybersecurity.training.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Autowired
    private UserCourseRegistrationRepository userCourseRegistrationRepository;
    @Autowired
    private UserLessonProgressRepository userLessonProgressRepository;
    @Autowired
    private UserService userService;

    public void createLesson(ReqLessonDTO dto) {
        if (dto.getLessonName() == null || dto.getLessonName().trim().isEmpty()) {
            throw new IllegalArgumentException("Lesson name is required");
        }
        if (dto.getModuleId() == null) {
            throw new IllegalArgumentException("Module ID is required");
        }
        Module module = moduleService.getModuleByModuleId(dto.getModuleId());

        Lesson lesson = new Lesson();
        lesson.fromRequestDto(dto);
        lesson.setModule(module);

        lesson = lessonRepository.save(lesson);

        LessonContent content = new LessonContent();
        content.fromRequestDto(dto);
        content.setLesson(lesson);

        lessonContentRepository.save(content);

        updateUserLessonProgressForNewLesson(lesson);
    }

    public ResultPaginationDTO<ResLessonDTO> getPagingLesson(Specification<Lesson> spec, Pageable pageable) {
        Page<Lesson> pageLesson = lessonRepository.findAll(spec, pageable);

        ResultPaginationDTO<ResLessonDTO> res = new ResultPaginationDTO<>();
        res.setMetaAndResponse(pageLesson);

        return res;
    }

    public List<Lesson> getListLessons(Specification<Lesson> spec) {
        return lessonRepository.findAll(spec);
    }

    public List<Lesson> getListLessonsByCourseId(Long courseId) {
        return lessonRepository.findAllByModule_Course_CourseId(courseId);
    }

    public LessonContent getLessonDetailsByLessonId(Long lessonId) {
        return this.lessonContentRepository.findByLesson_LessonId(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson Details not found!"));
    }

    public LessonContent getLessonDetailsByCourseAndLessonId(Long courseId, Long lessonId) {
        return this.lessonContentRepository.findByLesson_LessonIdAndLesson_Module_Course_CourseId(lessonId,courseId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson for Course not found!"));
    }

    public Lesson getLessonByLessonId(Long lessonId) {
        return this.lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson Id not found!"));
    }

    @Transactional
    public Lesson updateLesson(Long lessonId, ReqLessonDTO dto) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson ID not found"));

        boolean isChange = !dto.getLessonName().trim().equals(lesson.getLessonName());

        if(lesson.getModule() == null || !dto.getModuleId().equals(lesson.getModule().getModuleId())) {
            lesson.setModule(this.moduleService.getModuleByModuleId(dto.getModuleId()));
            isChange = true;
        }

        if(dto.getContent() != null && !dto.getContent().isEmpty()) {
            LessonContent content = lessonContentRepository.findLessonContentByLesson(lesson)
                            .orElseGet(() -> {
                                var newContent = new LessonContent();
                                newContent.setLesson(lesson);
                                return newContent;
                            });
            content.setContent(dto.getContent());
            lessonContentRepository.save(content);
        }

        if(isChange) {
            lesson.fromRequestDto(dto);
            lessonRepository.save(lesson);
        }

        return lessonRepository.findById(lessonId).get();
    }

    private void updateUserLessonProgressForNewLesson(Lesson lesson) {
        Long courseId = lesson.getModule().getCourse().getCourseId();

        List<UserCourseRegistration> registeredUsers = userCourseRegistrationRepository.findAllByCourse_CourseId(courseId);

        for (UserCourseRegistration registration : registeredUsers) {
            User user = registration.getUser();

            boolean exists = userLessonProgressRepository.existsByLesson_LessonIdAndUser_UserId(lesson.getLessonId(), user.getUserId());

            if (!exists) {
                UserLessonProgress newProgress = new UserLessonProgress();
                newProgress.setUser(user);
                newProgress.setLesson(lesson);
                newProgress.setComplete(false);

                userLessonProgressRepository.save(newProgress);
            }
        }
    }

    @Transactional
    public void deleteLesson(Long lessonId) {
        this.lessonContentRepository.deleteByLesson_LessonId(lessonId);
        this.lessonRepository.deleteById(lessonId);
    }

}



