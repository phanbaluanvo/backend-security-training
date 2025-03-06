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
public class QuizQuestion {

    @EmbeddedId
    private QuizQuestionId quizQuestionId;

    @MapsId("quizId")
    @ManyToOne
    @JoinColumn(name = "quizId")
    private Quiz quiz;

    @MapsId("questionId")
    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;

}
