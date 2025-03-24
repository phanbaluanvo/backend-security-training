package kpu.cybersecurity.training.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kpu.cybersecurity.training.domain.dto.request.ReqQuizDTO;
import kpu.cybersecurity.training.domain.dto.response.ResQuizDTO;
import kpu.cybersecurity.training.domain.dto.response.ResultPaginationDTO;
import kpu.cybersecurity.training.domain.entity.*;
import kpu.cybersecurity.training.domain.entity.id.QuizQuestionId;
import kpu.cybersecurity.training.repository.QuizRepository;
import kpu.cybersecurity.training.util.exception.UniqueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private QuestionService questionService;

    @Transactional
    public void createQuiz(ReqQuizDTO dto) throws UniqueException {
        checkExistsUniqueName(dto.getQuizName().trim());

        Course course = courseService.getCourseByCourseId(dto.getCourseId());

        Quiz quiz = new Quiz();
        quiz.fromRequestDto(dto);
        quiz.setCourse(course);
        final Quiz finalQuiz = quizRepository.save(quiz);

        if (dto.getQuestions() != null) {
            dto.getQuestions().forEach(questionDto -> {
                Question question = questionService.createQuestion(questionDto);

                QuizQuestionId quizQuestionId = new QuizQuestionId(finalQuiz.getQuizId(), question.getQuestionId());
                QuizQuestion quizQuestion = new QuizQuestion(quizQuestionId, finalQuiz, question);
                finalQuiz.getQuizQuestions().add(quizQuestion);
            });
        }
    }

    public Quiz getQuizByQuizId(Long quizId) {
        return quizRepository.findQuizByQuizId(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz ID not found"));
    }

    public ResultPaginationDTO<ResQuizDTO> getPagingQuizzes(Specification<Quiz> spec, Pageable pageable) {
        Page<Quiz> pageQuiz = quizRepository.findAll(spec, pageable);

        ResultPaginationDTO<ResQuizDTO> res = new ResultPaginationDTO<>();
        res.setMetaAndResponse(pageQuiz);

        return res;
    }

    public List<ResQuizDTO> getListQuizzes(Specification<Quiz> spec) {
        return quizRepository.findAll(spec)
                .stream().map(Quiz::toResponseDto)
                .toList();
    }

    @Transactional
    public Quiz updateQuiz(Long quizId, ReqQuizDTO dto) throws UniqueException {
        Quiz quiz = quizRepository.findQuizByQuizId(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz ID not found"));

        boolean isChange = false;

        if (!dto.getQuizName().trim().equals(quiz.getQuizName())) {
            checkExistsUniqueName(dto.getQuizName().trim());
            isChange = true;
        }

        if (!dto.getDescription().equals(quiz.getDescription()) ||
                dto.isActive() != quiz.isActive()) {
            isChange = true;
        }

        if (!quiz.getCourse().getCourseId().equals(dto.getCourseId())) {
            Course course = courseService.getCourseByCourseId(dto.getCourseId());
            quiz.setCourse(course);
            isChange = true;
        }

        if (isChange) {
            quiz.fromRequestDto(dto);
            quizRepository.save(quiz);
        }

        return quiz;
    }

    @Transactional
    public void removeQuestionFromQuiz(Long quizId, Long questionId) {
        Quiz quiz = this.getQuizByQuizId(quizId);
        quiz.getQuizQuestions().removeIf(qq -> qq.getQuizQuestionId().getQuestionId().equals(questionId));
        quizRepository.save(quiz);
    }

    @Transactional
    public void deleteQuiz(Long quizId) {
        quizRepository.deleteById(quizId);
    }

    private void checkExistsUniqueName(String name) throws UniqueException {
        if (quizRepository.existsQuizByQuizName(name)) {
            throw new UniqueException("Quiz name already exists");
        }
    }
}