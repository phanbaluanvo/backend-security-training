package kpu.cybersecurity.training.controller;

import com.turkraft.springfilter.boot.Filter;
import kpu.cybersecurity.training.domain.dto.request.ReqTopicDTO;
import kpu.cybersecurity.training.domain.dto.response.ResTopicDTO;
import kpu.cybersecurity.training.domain.dto.response.ResultPaginationDTO;
import kpu.cybersecurity.training.domain.entity.Topic;
import kpu.cybersecurity.training.service.TopicService;
import kpu.cybersecurity.training.util.annotation.ApiMessage;
import kpu.cybersecurity.training.util.exception.UniqueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @PostMapping("/create")
    @ApiMessage("Create Topics")
    private ResponseEntity<Void> createTopic(@RequestBody ReqTopicDTO dto) throws UniqueException {
        topicService.createTopic(dto);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/get")
    @ApiMessage("Get Paging Topics")
    private ResponseEntity<ResultPaginationDTO<ResTopicDTO>> getPagingTopics(
            Pageable pageable,
            @Filter Specification<Topic> spec
            ) {

        return ResponseEntity.ok(topicService.getPagingTopic(spec, pageable));
    }

    @GetMapping("/get/list")
    @ApiMessage("Get List Topics")
    private ResponseEntity<List<ResTopicDTO>> getListTopics(
            @Filter Specification<Topic> spec
    ) {
        return ResponseEntity.ok(topicService.getListTopics(spec));
    }

    @GetMapping("/get/{topicId}")
    @ApiMessage("Get Topic by Topic ID")
    private ResponseEntity<ResTopicDTO> getTopic(
            @PathVariable("topicId") Long topicId
    ) {
        return ResponseEntity.ok(topicService.getTopicByTopicId(topicId).toResponseDto());
    }

    @PutMapping("/update/{topicId}")
    @ApiMessage("Update Topic")
    private ResponseEntity<ResTopicDTO> updateTopic(
            @PathVariable("topicId") Long topicId,
            @RequestBody ReqTopicDTO dto
    ) throws UniqueException {
        return ResponseEntity.ok(topicService.updateTopic(topicId, dto).toResponseDto());
    }

    @DeleteMapping("/delete/{topicId}")
    @ApiMessage("Delete Topic")
    private ResponseEntity<Void> deleteTopic(
            @PathVariable("topicId") Long topicId
    ) {
        topicService.deleteTopic(topicId);
        return ResponseEntity.ok(null);
    }
}
