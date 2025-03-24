package kpu.cybersecurity.training.repository;

import kpu.cybersecurity.training.domain.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long>, JpaSpecificationExecutor<Quiz> {
    boolean existsQuizByQuizName(String quizName);

    Optional<Quiz> findQuizByQuizId(Long quizId);
}