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

    /*
     * Step 1: Create a Custom Delete Method in the StudentRepository
     * In your StudentRepository, you'll need to create a method to delete all
     * students whose yearOfEnrollment matches a given year. Spring Data JPA allows
     * you to define custom queries for deleting records using the @Query
     * annotation.
     * 
     * StudentRepository.java
     * java
     * Copy code
     * public interface StudentRepository extends JpaRepository<Student, Long> {
     * 
     * // Custom query to delete all students who enrolled in a specific year
     * 
     * @Modifying
     * 
     * @Transactional
     * 
     * @Query("DELETE FROM Student s WHERE s.yearOfEnrollment = :year")
     * void deleteStudentsByYearOfEnrollment(@Param("year") String year);
     * }
     * 
     * @Modifying: This annotation indicates that the query will modify the data
     * (i.e., delete).
     * 
     * @Transactional: This ensures that the delete operation is performed within a
     * transaction.
     * 
     * @Query: The custom query deletes all Student records where yearOfEnrollment
     * matches the given year.
     * Step 2: Create a Service Method to Call the Repository
     * Next, you'll need to create a service method that calls this repository
     * method to delete the students.
     * 
     * StudentService.java
     * java
     * Copy code
     * public interface StudentService {
     * 
     * // Other methods...
     * 
     * void deleteStudentsByYearOfEnrollment(String year);
     * }
     * StudentServiceImpl.java
     * java
     * Copy code
     * 
     * @Service
     * public class StudentServiceImpl implements StudentService {
     * 
     * @Autowired
     * private StudentRepository studentRepository;
     * 
     * // Other methods...
     * 
     * @Override
     * public void deleteStudentsByYearOfEnrollment(String year) {
     * studentRepository.deleteStudentsByYearOfEnrollment(year);
     * }
     * }
     * Step 3: Create the Controller Method to Handle the DELETE Request
     * In the controller, you'll expose an endpoint to allow deletion of all
     * students who enrolled in a specific year. This endpoint will use the DELETE
     * HTTP method.
     * 
     * StudentController.java
     * java
     * Copy code
     * 
     * @RestController
     * 
     * @RequestMapping("/api/students")
     * 
     * @CrossOrigin(origins = "*")
     * public class StudentController {
     * 
     * @Autowired
     * private StudentService studentService;
     * 
     * // Endpoint to delete students by their year of enrollment
     * 
     * @DeleteMapping("/deleteByYear/{year}")
     * public ResponseEntity<String> deleteStudentsByYear(@PathVariable("year")
     * String year) {
     * studentService.deleteStudentsByYearOfEnrollment(year);
     * 
     * return new ResponseEntity<>("Students who enrolled in " + year +
     * " have been deleted.", HttpStatus.OK);
     * }
     * }
     * 
     * @DeleteMapping("/deleteByYear/{year}"): This maps the DELETE request to the
     * URL /api/students/deleteByYear/{year}, where {year} is a path variable
     * representing the year of enrollment.
     * 
     * @PathVariable("year"): This binds the year from the URL to the method
     * argument.
     * The method calls deleteStudentsByYearOfEnrollment(year) in the service to
     * perform the delete operation.
     * The response returns a message indicating that the students who enrolled in
     * the given year have been deleted.
     * Step 4: Test the DELETE Endpoint
     * Once everything is set up, you can test the delete functionality using
     * Postman or curl.
     * 
     * Example Request:
     * bash
     * Copy code
     * DELETE http://localhost:8080/api/students/deleteByYear/2022
     * This request will delete all students who enrolled in the year 2022.
     * 
     * Expected Response:
     * json
     * Copy code
     * "Students who enrolled in 2022 have been deleted."
     * If there are no students found with that enrollment year, it will still
     * return the success message because the delete query will simply not affect
     * any rows in that case.
     * 
     * Recap of Changes:
     * Repository:
     * 
     * Added a custom @Query to delete students based on their yearOfEnrollment.
     * Used @Modifying and @Transactional annotations to make sure the delete
     * operation works properly.
     * Service:
     * 
     * Created a service method deleteStudentsByYearOfEnrollment to call the
     * repository method.
     * Controller:
     * 
     * Added a DELETE endpoint /api/students/deleteByYear/{year} that allows the
     * deletion of students who enrolled in the specified year.
     * Final URL to Delete Students by Year of Enrollment:
     * bash
     * Copy code
     * DELETE http://localhost:8080/api/students/deleteByYear/{year}
     * Example:
     * If you want to delete all students who enrolled in the year 2023, you would
     * send a DELETE request to:
     * 
     * bash
     * Copy code
     * DELETE http://localhost:8080/api/students/deleteByYear/2023
     */
}
