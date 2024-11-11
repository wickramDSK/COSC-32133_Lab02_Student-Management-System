package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // CRUD
    // method for find students from enrollment yr
    List<Student> findStudentByYearOfEnrollment(String yearOfEnrollment);

}
