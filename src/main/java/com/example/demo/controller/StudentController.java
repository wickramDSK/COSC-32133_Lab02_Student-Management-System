package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<Student> saveStudent(@RequestBody Student student) {
        return new ResponseEntity<Student>(studentService.saveStudent(student), HttpStatus.CREATED);
    }

    // GetAll Rest Api
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Get by Id Rest Api
    @GetMapping("{id}")
    // localhost:8080/api/students/1
    public ResponseEntity<Student> getStudentById(@PathVariable("id") long studentID) {
        return new ResponseEntity<Student>(studentService.getStudentById(studentID), HttpStatus.OK);
    }

    // Update Rest Api
    @PutMapping("{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") long id, @RequestBody Student student) {
        return new ResponseEntity<Student>(studentService.updateStudent(student, id), HttpStatus.OK);
    }

    // Delete Rest Api
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable("id") long id) {
        // delete student from db
        studentService.deleteStudent(id);
        return new ResponseEntity<String>("Student deleted Successfully.", HttpStatus.OK);
    }

    // find student by enrollment year
    @GetMapping("/getByYear/{year}")
    public ResponseEntity<List<Student>> getStudentByYear(@PathVariable("year") String yearOfEnrollment) {
        List<Student> students = studentService.getStudentByYear(yearOfEnrollment);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // find department by student id
    @GetMapping("/getDepByID/{id}")
    public ResponseEntity<String> getDepartmentByStudentId(@PathVariable("id") long id) {
        // Get the department by student ID using the service
        String department = studentService.getDepartmentByStudentId(id);

        if (department == null) {
            // Return 404 if the student is not found
            return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
        }

        // Return the department with 200 OK status
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    // delete record by year
    @DeleteMapping("/deleteByYear/{year}")
    public ResponseEntity<String> deleteStudentsByYear(@PathVariable("year") String year) {
        studentService.deleteStudentsByYearOfEnrollment(year);

        return new ResponseEntity<>("Students who enrolled in " + year + " have been deleted.", HttpStatus.OK);
    }

    // DELETE http://localhost:8080/api/students/deleteByYear/2022
}
