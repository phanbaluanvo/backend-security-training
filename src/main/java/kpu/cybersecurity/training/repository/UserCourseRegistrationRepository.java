package kpu.cybersecurity.training.repository;

import kpu.cybersecurity.training.domain.dto.response.ResUserCourseProgressDTO;
import kpu.cybersecurity.training.domain.entity.UserCourseRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserCourseRegistrationRepository extends JpaRepository<UserCourseRegistration,Long>, JpaSpecificationExecutor<UserCourseRegistration> {

    @Modifying
    @Query("UPDATE UserCourseRegistration u SET u.isComplete = :isComplete, u.completionDate = :completionDate" +
            " WHERE u.course.courseId = :courseId AND u.user.userId = :userId")
    void updateCompletionStatusByUserIdAndCourseId(@Param("userId") String userId,
                                                   @Param("courseId") Long courseId,
                                                   @Param("isComplete") boolean isComplete,
                                                   @Param("completionDate")LocalDate completionDate);

    @Query("Select new kpu.cybersecurity.training.domain.dto.response.ResUserCourseProgressDTO(" +
            "u.user.userId, u.course.courseId, u.course.courseName, u.isComplete, u.completionDate) " +
            "FROM UserCourseRegistration u " +
            "WHERE u.user.userId = :userId AND u.course.courseId = :courseId")
   ResUserCourseProgressDTO getUserCourseProgressByUserIdAndCourseId(@Param("userId") String userId, @Param("courseId") Long courseId);

    boolean existsByUser_UserIdAndCourse_CourseId(String userId, Long courseId);

    List<UserCourseRegistration> findAllByCourse_CourseId(Long courseId);
}
