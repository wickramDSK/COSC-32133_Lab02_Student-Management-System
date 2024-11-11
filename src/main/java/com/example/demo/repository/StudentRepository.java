package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // CRUD
    // method for find students from enrollment yr
    List<Student> findStudentByYearOfEnrollment(String yearOfEnrollment);

    // query for find the department of student by ID
    @Query("SELECT s.department FROM Student s WHERE s.id = :id")
    String findDepartmentById(@Param("id") long id);
}
