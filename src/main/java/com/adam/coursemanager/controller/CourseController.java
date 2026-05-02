package com.adam.coursemanager.controller;

import com.adam.coursemanager.entity.Course;
import com.adam.coursemanager.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String listCourses(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "list-courses";
    }

    @GetMapping("/new")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "add-course";
    }

    @PostMapping
    public String addCourse(@ModelAttribute("course") Course course) {
        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        model.addAttribute("course", course);
        return "update-course";
    }

    @PostMapping("/update/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute("course") Course course) {
        courseService.updateCourse(id, course);
        return "redirect:/courses";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }
}
