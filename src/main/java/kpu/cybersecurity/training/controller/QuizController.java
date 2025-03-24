package kpu.cybersecurity.training.controller;

import com.turkraft.springfilter.boot.Filter;
import kpu.cybersecurity.training.domain.dto.request.ReqQuizDTO;
import kpu.cybersecurity.training.domain.dto.response.ResQuizDTO;
import kpu.cybersecurity.training.domain.dto.response.ResultPaginationDTO;
import kpu.cybersecurity.training.domain.entity.Quiz;
import kpu.cybersecurity.training.service.QuizService;
import kpu.cybersecurity.training.util.annotation.ApiMessage;
import kpu.cybersecurity.training.util.exception.UniqueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("/create")
    @ApiMessage("Create quiz successfully")
    public ResponseEntity<Void> createQuiz(@RequestBody ReqQuizDTO dto) throws UniqueException {
        quizService.createQuiz(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{quizId}")
    public ResponseEntity<ResQuizDTO> getQuizById(@PathVariable("quizId") Long quizId) {
        Quiz quiz = quizService.getQuizByQuizId(quizId);
        return ResponseEntity.ok(quiz.toResponseDto());
    }

    @GetMapping("/paging")
    public ResponseEntity<ResultPaginationDTO<ResQuizDTO>> getPagingQuizzes(
            @Filter Specification<Quiz> spec,
            Pageable pageable) {
        return ResponseEntity.ok(quizService.getPagingQuizzes(spec, pageable));
    }

    @GetMapping
    public ResponseEntity<List<ResQuizDTO>> getListQuizzes(
            @Filter Specification<Quiz> spec) {
        return ResponseEntity.ok(quizService.getListQuizzes(spec));
    }

    @PutMapping("/{id}")
    @ApiMessage("Update quiz successfully")
    public ResponseEntity<ResQuizDTO> updateQuiz(
            @PathVariable("id") Long quizId,
            @RequestBody ReqQuizDTO dto) throws UniqueException {
        Quiz quiz = quizService.updateQuiz(quizId, dto);
        return ResponseEntity.ok(quiz.toResponseDto());
    }

    @DeleteMapping("/{quizId}/questions/{questionId}")
    @ApiMessage("Remove question from quiz successfully")
    public ResponseEntity<Void> removeQuestionFromQuiz(
            @PathVariable Long quizId,
            @PathVariable Long questionId) {
        quizService.removeQuestionFromQuiz(quizId, questionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete quiz successfully")
    public ResponseEntity<Void> deleteQuiz(@PathVariable("id") Long quizId) {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.ok().build();
    }
}