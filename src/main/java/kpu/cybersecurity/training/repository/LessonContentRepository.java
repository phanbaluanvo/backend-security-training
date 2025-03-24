package kpu.cybersecurity.training.repository;

import kpu.cybersecurity.training.domain.entity.Lesson;
import kpu.cybersecurity.training.domain.entity.LessonContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface LessonContentRepository extends JpaRepository<LessonContent, Long>, JpaSpecificationExecutor<LessonContent> {

    Optional<LessonContent> findLessonContentByLesson(Lesson lesson);

    Optional<LessonContent> findByLesson_LessonId(Long lessonId);

    Optional<LessonContent> findByLesson_LessonIdAndLesson_Module_Course_CourseId(Long lessonId, Long courseId);

    void deleteByLesson_LessonId(Long lessonId);
}
