package kpu.cybersecurity.training.domain.entity.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestionId implements Serializable {
    private Long quizId;
    private Long questionId;
}
