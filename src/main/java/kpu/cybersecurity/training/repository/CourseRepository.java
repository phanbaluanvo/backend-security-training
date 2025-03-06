package kpu.cybersecurity.training.repository;

import kpu.cybersecurity.training.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    boolean existsCourseByCourseName(String courseName);
    Optional<Course> findCourseByCourseId(Long courseId);

    @Modifying
    @Query(value = """
    UPDATE course
    SET topic_id =
    CASE
        WHEN :newTopicId IS NULL THEN NULL
        ELSE :newTopicId
    END
    WHERE (:oldTopicId IS NULL AND topic_id IS NULL) 
       OR (:oldTopicId IS NOT NULL AND topic_id = :oldTopicId)
       AND (:courseIds IS NULL OR course_id IN (:courseIds))
    """, nativeQuery = true)
    void assignNewTopicToCourse(
            @Param("oldTopicId") Long oldTopicId,
            @Param("newTopicId") Long newTopicId,
            @Param("courseIds") List<Long> courseIds
    );

}
