package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Student;

import jakarta.transaction.Transactional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // CRUD
    // method for find students from enrollment yr
    List<Student> findStudentByYearOfEnrollment(String yearOfEnrollment);

    // query for find the department of student by ID
    @Query("SELECT s.department FROM Student s WHERE s.id = :id")
    String findDepartmentById(@Param("id") long id);

    // Custom query to delete all students who enrolled in a specific year
    @Modifying
    @Transactional
    @Query("DELETE FROM Student s WHERE s.yearOfEnrollment = :year")
    void deleteStudentsByYearOfEnrollment(@Param("year") String year);
}
