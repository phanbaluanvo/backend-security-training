package kpu.cybersecurity.training.repository;

import kpu.cybersecurity.training.domain.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long>, JpaSpecificationExecutor<Module> {

    boolean existsModuleByModuleName(String moduleName);

    Optional<Module> findModuleByModuleId(Long moduleId);

    @Modifying
    @Query(value = """
    UPDATE module
    SET course_id =
    CASE
        WHEN :newCourseId IS NULL THEN NULL
        ELSE :newCourseId
    END
    WHERE (:oldCourseId IS NULL AND course_id IS NULL) 
       OR (:oldCourseId IS NOT NULL AND course_id = :oldCourseId)
       AND (:moduleIds IS NULL OR module_id IN (:moduleIds))
    """, nativeQuery = true)
    void assignNewCourseToModule(
            @Param("oldCourseId") Long oldCourseId,
            @Param("newCourseId") Long newCourseId,
            @Param("moduleIds") List<Long> moduleIds
    );




}
