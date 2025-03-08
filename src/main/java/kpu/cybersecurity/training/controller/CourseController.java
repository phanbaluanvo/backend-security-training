package kpu.cybersecurity.training.controller;

import com.turkraft.springfilter.boot.Filter;
import kpu.cybersecurity.training.domain.dto.request.ReqCourseDTO;
import kpu.cybersecurity.training.domain.dto.response.ResCourseDTO;
import kpu.cybersecurity.training.domain.dto.response.ResultPaginationDTO;
import kpu.cybersecurity.training.domain.entity.Course;
import kpu.cybersecurity.training.service.CourseService;
import kpu.cybersecurity.training.util.annotation.ApiMessage;
import kpu.cybersecurity.training.util.exception.UniqueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/create")
    @ApiMessage("Create Course")
    private ResponseEntity<Void> createCourse(@RequestBody ReqCourseDTO dto) throws UniqueException {
        courseService.createCourse(dto);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/get")
    @ApiMessage("Get Paging Courses")
    private ResponseEntity<ResultPaginationDTO<ResCourseDTO>> getPagingCourses(
            Pageable pageable,
            @Filter Specification<Course> spec
    ) {
        return ResponseEntity.ok(courseService.getPagingCourses(spec, pageable));
    }

    @GetMapping("/get/list")
    @ApiMessage("Get List Courses")
    private ResponseEntity<List<ResCourseDTO>> getListCourses(
            @Filter Specification<Course> spec
    ) {
        return ResponseEntity.ok(courseService.getListCourses(spec));
    }

    @GetMapping("/get/{courseId}")
    @ApiMessage("Get Course by CourseID")
    private ResponseEntity<ResCourseDTO> getCourseByCourseId(
            @PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(courseService.getCourseByCourseId(courseId).toResponseDto());
    }

    @PutMapping("/update/{courseId}")
    @ApiMessage("Update Course")
    private ResponseEntity<ResCourseDTO> updateCourse(
            @PathVariable("courseId") Long courseId,
            @RequestBody ReqCourseDTO dto
    ) throws UniqueException {
        return ResponseEntity.ok(courseService.updateCourse(courseId, dto).toResponseDto());
    }

    @DeleteMapping("/delete/{courseId}")
    @ApiMessage("Delete Course")
    private ResponseEntity<Void> deleteCourse(@PathVariable("courseId") Long courseId) {
        this.courseService.deleteCourse(courseId);
        return ResponseEntity.ok(null);
    }
}
