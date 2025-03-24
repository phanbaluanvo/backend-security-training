package kpu.cybersecurity.training.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import kpu.cybersecurity.training.config.EnvProperties;
import kpu.cybersecurity.training.domain.dto.request.ReqUserLessonProgressDTO;
import kpu.cybersecurity.training.domain.dto.response.ResModuleLessonProgressDTO;
import kpu.cybersecurity.training.domain.dto.response.ResUserCourseProgressDTO;
import kpu.cybersecurity.training.domain.entity.*;
import kpu.cybersecurity.training.repository.ModuleRepository;
import kpu.cybersecurity.training.repository.UserCourseRegistrationRepository;
import kpu.cybersecurity.training.repository.UserLessonProgressRepository;
import kpu.cybersecurity.training.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;


import javax.swing.text.html.Option;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class LearnProcess {
    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private EnvProperties envProperties;

    @Autowired
    private UserCourseRegistrationRepository userCourseRegistrationRepository;

    @Autowired
    private UserLessonProgressRepository userLessonProgressRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public void registerCourse(String userId, Long courseId) {
        User user = userService.getUserByUserID(userId);
        Course course = courseService.getCourseByCourseId(courseId);

        if(isUserRegisteredForCourse(userId,courseId))
            throw new EntityExistsException("User already registered for course");

        UserCourseRegistration userRegisterCourse = new UserCourseRegistration();
        userRegisterCourse.setUser(user);
        userRegisterCourse.setCourse(course);
        userRegisterCourse.setRegistrationDate(Instant.now());

        List<Lesson> lessonsInCourse = lessonService.getListLessonsByCourseId(courseId);

        this.userCourseRegistrationRepository.save(userRegisterCourse);

        lessonsInCourse.forEach(lesson -> {
            UserLessonProgress progress = new UserLessonProgress();
            progress.setUser(user);
            progress.setLesson(lesson);
            this.userLessonProgressRepository.save(progress);
        });
    }

    public void registerCourse(Long courseId) {
        String userId = SecurityUtil.getCurrentUserLogin().orElseThrow(() -> new BadCredentialsException("Invalid User"));
        registerCourse(userId, courseId);
    }

    private boolean isUserRegisteredForCourse(String userId, Long courseId) {
        return this.userCourseRegistrationRepository.existsByUser_UserIdAndCourse_CourseId(userId, courseId);
    }

    public void updateCourseLessonProgress(String userId, Long lessonId, Double completePercentage) {
        Optional<UserLessonProgress> progressOpt = userLessonProgressRepository.findByUser_UserIdAndLesson_LessonId(userId, lessonId);

        UserLessonProgress progress;

        if (progressOpt.isPresent()) {
            progress = progressOpt.get();

        } else {
            progress = new UserLessonProgress();
            User user = userService.getUserByUserID(userId);
            progress.setUser(user);
            progress.setLesson(this.lessonService.getLessonByLessonId(lessonId));
        }
        userLessonProgressRepository.save(progress);
    }

    private boolean checkCourseCompletion(String userId, Long courseId) {
        List<Lesson> lessonsInCourse = lessonService.getListLessonsByCourseId(courseId);

        // Get list user progress for that course
        List<UserLessonProgress> userProgressList = userLessonProgressRepository.findAllByUser_UserIdAndLesson_Module_Course_CourseId(userId, courseId);

        // Check completionStatus
        return lessonsInCourse.stream()
                .allMatch(lesson -> userProgressList.stream()
                        .anyMatch(progress -> progress.getLesson().getLessonId().equals(lesson.getLessonId())
                                && progress.isComplete()));
    }

    private void updateCourseCompletionStatus(String userId, Long courseId, boolean isComplete) {
        LocalDate now = LocalDate.now(ZoneId.of(envProperties.getJsonFormat().getTimeZone()));
        this.userCourseRegistrationRepository.updateCompletionStatusByUserIdAndCourseId(userId, courseId, isComplete, now);
    }

    public void trackingLessonCourseProgress(ReqUserLessonProgressDTO dto) {

        String userId = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new BadCredentialsException("Invalid User ID"));

        updateCourseLessonProgress(userId, dto.getLessonId(), dto.getCompletePercentage());

        if(checkCourseCompletion(userId, dto.getCourseId())){
            updateCourseCompletionStatus(userId, dto.getCourseId(), true);
        }
    }

    public ResUserCourseProgressDTO getUserCourseProgress(String userId, Long courseId) {
        if(!isUserRegisteredForCourse(userId, courseId))  {
            throw new EntityNotFoundException("You have not registered for this course");
        } else {
            var response = this.userCourseRegistrationRepository.getUserCourseProgressByUserIdAndCourseId(userId, courseId);
            var modules = this.moduleRepository.getModuleForProgress(courseId);

            modules.forEach(module -> {
                var lessons = this.userLessonProgressRepository.getLessonsProgressByUserIdAndCourseId(userId, module.getModuleId());
                module.setLessons(lessons);
            });
            response.setModules(modules);
            return response;
        }
    }

    public ResUserCourseProgressDTO getUserCourseProgress(Long courseId) {
        String userId = SecurityUtil.getCurrentUserLogin().orElseThrow(() -> new BadCredentialsException("Invalid User"));
        return getUserCourseProgress(userId, courseId);
    }



}
