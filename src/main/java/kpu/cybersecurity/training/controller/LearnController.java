package kpu.cybersecurity.training.controller;

import kpu.cybersecurity.training.domain.dto.request.ReqUserLessonProgressDTO;
import kpu.cybersecurity.training.domain.dto.response.ResUserCourseProgressDTO;
import kpu.cybersecurity.training.service.LearnProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/learn")
public class LearnController {

    @Autowired
    private LearnProcess learnProcess;

    @PostMapping("/register/courses/{courseId}")
    public ResponseEntity<Void> registerCourse(@PathVariable("courseId") Long courseId) {
        learnProcess.registerCourse(courseId);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/tracking")
    public ResponseEntity<Void> trackingLessonAndCourse(@RequestBody ReqUserLessonProgressDTO dto) {
        learnProcess.trackingLessonCourseProgress(dto);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/get/courses/{courseId}")
    public ResponseEntity<ResUserCourseProgressDTO> getUserCourseProgress(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(learnProcess.getUserCourseProgress(courseId));
    }

}