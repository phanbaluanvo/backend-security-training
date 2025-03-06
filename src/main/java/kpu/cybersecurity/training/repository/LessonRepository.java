package kpu.cybersecurity.training.repository;

import kpu.cybersecurity.training.domain.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {
    @Modifying
    @Query(value = """
    UPDATE lesson
    SET module_id =
    CASE
        WHEN :newModuleId IS NULL THEN NULL
        ELSE :newModuleId
    END
    WHERE (:oldModuleId IS NULL AND module_id IS NULL) 
       OR (:oldModuleId IS NOT NULL AND module_id = :oldModuleId)
       AND (:lessonIds IS NULL OR lesson_id IN (:lessonIds))
    """, nativeQuery = true)
    void assignNewModuleToLesson(
            @Param("oldModuleId") Long oldModuleId,
            @Param("newModuleId") Long newModuleId,
            @Param("lessonIds") List<Long> lessonIds
    );
}
