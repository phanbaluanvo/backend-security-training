package kpu.cybersecurity.training.service;

import jakarta.persistence.EntityNotFoundException;
import kpu.cybersecurity.training.domain.dto.request.ReqQuestionDTO;
import kpu.cybersecurity.training.domain.entity.Answer;
import kpu.cybersecurity.training.domain.entity.Question;
import kpu.cybersecurity.training.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public Question getQuestionByQuestionId(Long questionId) {
        return questionRepository.findQuestionByQuestionId(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question ID not found"));
    }

    public Question createQuestion(ReqQuestionDTO dto) {
        Question question = new Question();
        question.fromRequestDto(dto);

        if (dto.getAnswers() != null) {
            dto.getAnswers().forEach(answerDto -> {
                Answer answer = new Answer();
                answer.fromRequestDto(answerDto);
                answer.setQuestion(question);
                question.getAnswers().add(answer);
            });
        }
        return questionRepository.save(question);
    }
}