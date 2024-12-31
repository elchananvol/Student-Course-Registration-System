package com.system.service;

import com.system.exceptions.UserExceptions;
import com.system.model.*;
import com.system.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Transactional
    public CourseResponse createCourse(CourseCreationRequest courseCreationRequest) {
        Course course = Course.builder().name(courseCreationRequest.getName()).description(courseCreationRequest.getDescription()).build();
        course = courseRepository.save(course);
        return mapToCourseResponse(course);
    }

    public List<CourseResponse> getAllCoursesWithStudents() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(this::mapToCourseResponse).toList();
    }

    public CourseResponse getCourseById(Integer courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new UserExceptions.NotFound("Course not found with id: " + courseId));

        return mapToCourseResponse(course);
    }

    @Transactional
    public CourseResponse updateCourse(Integer courseId, CourseUpdateRequest updateRequest) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new UserExceptions.NotFound("Course not found with id: " + courseId));

        if (updateRequest.getName() != null) {
            course.setName(updateRequest.getName());
        }
        if (updateRequest.getDescription() != null) {
            course.setDescription(updateRequest.getDescription());
        }
        Course updatedCourse = courseRepository.save(course);
        return mapToCourseResponse(updatedCourse);
    }

    @Transactional
    public void deleteCourse(Integer courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new UserExceptions.NotFound("Course not found with id: " + courseId));
        courseRepository.delete(course);
    }

    private CourseResponse mapToCourseResponse(Course course) {
        CourseResponse CourseResponse = new CourseResponse(course.getId(), course.getName(), course.getDescription());
        CourseResponse.setEnrolledStudentIds(course.getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toList()));
        return CourseResponse;
    }

}
