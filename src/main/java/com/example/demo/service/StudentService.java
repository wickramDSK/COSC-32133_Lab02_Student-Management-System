package com.example.demo.service;

import com.example.demo.model.Student;
import java.util.List;

import org.springframework.data.repository.query.Param;

public interface StudentService {
    Student saveStudent(Student student);

    List<Student> getAllStudents();

    Student getStudentById(long id);

    Student updateStudent(Student student, long id);

    void deleteStudent(long id);

    List<Student> getStudentByYear(String yearOfEnrollment);

    String getDepartmentByStudentId(@Param("id") long id);

    void deleteStudentsByYearOfEnrollment(String year);
}
