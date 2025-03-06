package kpu.cybersecurity.training.repository;

import kpu.cybersecurity.training.domain.entity.LessonContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LessonContentRepository extends JpaRepository<LessonContent, Long>, JpaSpecificationExecutor<LessonContent> {
}
