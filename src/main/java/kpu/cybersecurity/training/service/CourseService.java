package kpu.cybersecurity.training.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kpu.cybersecurity.training.domain.dto.request.ReqCourseDTO;
import kpu.cybersecurity.training.domain.dto.response.ResCourseDTO;
import kpu.cybersecurity.training.domain.dto.response.ResultPaginationDTO;
import kpu.cybersecurity.training.domain.entity.Course;
import kpu.cybersecurity.training.domain.entity.Topic;
import kpu.cybersecurity.training.repository.CourseRepository;
import kpu.cybersecurity.training.repository.ModuleRepository;
import kpu.cybersecurity.training.util.exception.UniqueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private TopicService topicService;

    public void createCourse(ReqCourseDTO dto) throws UniqueException {
        checkExistsUniqueName(dto.getCourseName().trim());

        Topic topic = topicService.getTopicByTopicId(dto.getTopicId());

        Course course = new Course();
        course.fromRequestDto(dto);
        course.setTopic(topic);

        courseRepository.save(course);
    }

    public Course getCourseByCourseId(Long courseId) {
        return courseRepository.findCourseByCourseId(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course ID not found"));
    }

    public ResultPaginationDTO<ResCourseDTO> getPagingCourses(Specification<Course> spec, Pageable pageable) {
        Page<Course> pageCourse = courseRepository.findAll(spec, pageable);

        ResultPaginationDTO<ResCourseDTO> res = new ResultPaginationDTO<>();
        res.setMetaAndResponse(pageCourse);

        return res;
    }

    public List<ResCourseDTO> getListCourses(Specification<Course> spec) {
        return courseRepository.findAll(spec)
                .stream().map(Course::toResponseDto)
                .toList();
    }

    @Transactional
    public Course updateCourse(Long courseId, ReqCourseDTO dto) throws UniqueException {
        Course course = courseRepository.findCourseByCourseId(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course ID not found"));

        if (!dto.getCourseName().equals(course.getCourseName())) {
            checkExistsUniqueName(dto.getCourseName());
        }

        course.fromRequestDto(dto);

        var currentTopic = (course.getTopic() != null) ? course.getTopic().getTopicId() : null;

        if (dto.getTopicId() != null) {
            if(!dto.getTopicId().equals(currentTopic))
                course.setTopic(this.topicService.getTopicByTopicId(dto.getTopicId()));
        } else course.setTopic(null);

        courseRepository.save(course);

        return course;
    }


    @Transactional
    public void deleteCourse(Long courseId) {
        this.moduleRepository.assignNewCourseToModule(courseId, null, null);
        this.courseRepository.deleteById(courseId);
    }

    public void checkExistsUniqueName(String name) throws UniqueException {
        if (courseRepository.existsCourseByCourseName(name)) throw new UniqueException("Course name already exists");
    }
}
