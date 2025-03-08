package kpu.cybersecurity.training.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kpu.cybersecurity.training.domain.dto.request.ReqTopicDTO;
import kpu.cybersecurity.training.domain.dto.response.ResTopicDTO;
import kpu.cybersecurity.training.domain.dto.response.ResultPaginationDTO;
import kpu.cybersecurity.training.domain.entity.Topic;
import kpu.cybersecurity.training.repository.CourseRepository;
import kpu.cybersecurity.training.repository.TopicRepository;
import kpu.cybersecurity.training.util.exception.UniqueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CourseRepository courseRepository;

    public void createTopic(ReqTopicDTO dto) throws UniqueException {
        if(topicRepository.existsByTopicName(dto.getTopicName())) {
            throw new UniqueException("Topic name already exist.");
        }
        Topic topic = new Topic();
        topic.setTopicName(dto.getTopicName().trim());
        topicRepository.save(topic);
    }

    public Topic getTopicByTopicId(Long topicId) {

        return topicRepository.findTopicByTopicId(topicId).orElseThrow(() -> new EntityNotFoundException("Topic ID not found"));
    }

    public ResultPaginationDTO<ResTopicDTO> getPagingTopic(Specification<Topic> spec, Pageable pageable) {
        Page<Topic> pageTopic = topicRepository.findAll(spec, pageable);

        ResultPaginationDTO<ResTopicDTO> res = new ResultPaginationDTO<>();
        res.setMetaAndResponse(pageTopic);
        return res;
    }

    public List<ResTopicDTO> getListTopics(Specification<Topic> spec) {
        return topicRepository.findAll(spec)
                .stream().map(Topic::toResponseDto)
                .toList();
    }

    @Transactional
    public Topic updateTopic(Long id, ReqTopicDTO dto) throws UniqueException {

        Topic topic =  topicRepository.findTopicByTopicId(id).orElseThrow(() -> new EntityNotFoundException("Topic ID not found!"));

        String newName = dto.getTopicName().trim();

        if(!newName.equals(topic.getTopicName())) {
            if(topicRepository.existsByTopicName(newName))
                throw new UniqueException("Topic name already exist.");
            else {
                topic.setTopicName(newName);
                topicRepository.save(topic);
            }
        }

        return topicRepository.findTopicByTopicId(id).get();
    }

    @Transactional
    public void deleteTopic(Long topicId) {
        courseRepository.assignNewTopicToCourse(topicId, null, null);
        topicRepository.deleteById(topicId);
    }

}
