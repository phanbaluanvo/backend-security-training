package kpu.cybersecurity.training.domain.entity;

import jakarta.persistence.*;
import kpu.cybersecurity.training.domain.entity.id.QuizQuestionId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quiz_question")
public class QuizQuestion {

    @EmbeddedId
    private QuizQuestionId quizQuestionId;

    @MapsId("quizId")
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @MapsId("questionId")
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
