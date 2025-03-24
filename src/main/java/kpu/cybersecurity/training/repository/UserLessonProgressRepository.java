package kpu.cybersecurity.training.repository;

import kpu.cybersecurity.training.domain.dto.response.ResLessonProgressDTO;
import kpu.cybersecurity.training.domain.dto.response.ResUserCourseProgressDTO;
import kpu.cybersecurity.training.domain.entity.UserLessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserLessonProgressRepository extends JpaRepository<UserLessonProgress,Long>, JpaSpecificationExecutor<UserLessonProgress> {

    Optional<UserLessonProgress> findByUser_UserIdAndLesson_LessonId(String userId, Long lessonId);

    List<UserLessonProgress> findAllByUser_UserIdAndLesson_Module_Course_CourseId(String userId, Long courseId);

    @Query("SELECT new kpu.cybersecurity.training.domain.dto.response.ResLessonProgressDTO(" +
            "u.lesson.lessonId, u.lesson.lessonName, u.isComplete) " +
            "FROM UserLessonProgress u " +
            "WHERE u.user.userId = :userId AND u.lesson.module.moduleId = :moduleId " +
            "ORDER BY u.lesson.lessonId ASC")
    List<ResLessonProgressDTO> getLessonsProgressByUserIdAndCourseId(@Param("userId") String userId, @Param("moduleId") Long moduleId);

    boolean existsByLesson_LessonIdAndUser_UserId(Long lessonId, String userId);

}
