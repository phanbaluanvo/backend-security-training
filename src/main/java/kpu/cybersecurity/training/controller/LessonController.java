package kpu.cybersecurity.training.controller;

import com.turkraft.springfilter.boot.Filter;
import kpu.cybersecurity.training.domain.dto.request.ReqLessonDTO;
import kpu.cybersecurity.training.domain.dto.response.ResLessonContentDTO;
import kpu.cybersecurity.training.domain.dto.response.ResLessonDTO;
import kpu.cybersecurity.training.domain.dto.response.ResultPaginationDTO;
import kpu.cybersecurity.training.domain.entity.Lesson;
import kpu.cybersecurity.training.service.LessonService;
import kpu.cybersecurity.training.util.annotation.ApiMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @PostMapping("/create")
    @ApiMessage("Create lesson")
    private ResponseEntity<Void> createLesson(@RequestBody ReqLessonDTO dto) {
        this.lessonService.createLesson(dto);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/get")
    @ApiMessage("Get Paging Lesson - without content")
    private ResponseEntity<ResultPaginationDTO<ResLessonDTO>> getPagingLesson(
            Pageable pageable,
            @Filter Specification<Lesson> spec
            ) {
        return ResponseEntity.ok(this.lessonService.getPagingLesson(spec,pageable));
    }

    @GetMapping("/get/list")
    @ApiMessage("Get List Lessons")
    private ResponseEntity<List<ResLessonDTO>> getListModules(
            @Filter Specification<Lesson> spec
    ) {
        return ResponseEntity.ok(lessonService.getListLessons(spec)
                .stream().map(Lesson::toResponseDto)
                .toList());
    }


    @GetMapping("/get/{lessonId}")
    @ApiMessage("Get Lesson Details")
    private ResponseEntity<ResLessonContentDTO> getLessonById(
            @PathVariable("lessonId") Long lessonId
    ) {
        return ResponseEntity.ok(this.lessonService.getLessonDetailsByLessonId(lessonId).toResponseDto());
    }

    @GetMapping("/get/courses/{courseId}/lessons/{lessonId}")
    @ApiMessage("Get Lesson Details by Course")
    private ResponseEntity<ResLessonContentDTO> getLessonByCourseAndLessonId(
            @PathVariable("courseId") Long courseId,
            @PathVariable("lessonId") Long lessonId
    ) {
        return ResponseEntity.ok(this.lessonService.getLessonDetailsByCourseAndLessonId(courseId, lessonId).toResponseDto());
    }

    @PutMapping("/update/{lessonId}")
    @ApiMessage("Update lesson")
    private ResponseEntity<ResLessonDTO> updateLesson(
            @PathVariable("lessonId") Long lessonId,
            @RequestBody ReqLessonDTO dto
    ) {
        return ResponseEntity.ok(this.lessonService.updateLesson(lessonId, dto).toResponseDto());
    }

    @DeleteMapping("/delete/{lessonId}")
    @ApiMessage("Delete Lesson")
    private ResponseEntity<Void> deleteLesson(
            @PathVariable("lessonId") Long lessonId
    ) {
        this.lessonService.deleteLesson(lessonId);
        return ResponseEntity.ok(null);
    }

}
