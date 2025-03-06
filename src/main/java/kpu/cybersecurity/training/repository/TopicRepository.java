package kpu.cybersecurity.training.repository;


import kpu.cybersecurity.training.domain.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>, JpaSpecificationExecutor<Topic> {
    boolean existsByTopicName(String topicName);

    Optional<Topic> findTopicByTopicId(Long topicId);

}
