package com.student.management.controller;

import com.student.management.exception.StudentNotFoundException;
import com.student.management.model.Student;
import com.student.management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    private int[] convertStringArrayToIntArray(String[] strings){
        int[] intIds = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            intIds[i] = Integer.parseInt(strings[i]);
        }
        return intIds;
    }

    @GetMapping("/students")
    public ResponseEntity<?> findStudent(@RequestParam(value = "classes", required = false) String classIds,
                                     @RequestParam(value = "active", required = false) String active,
                                     @RequestParam(value = "admissionYearAfter", required = false, defaultValue = "1900") String year,
                                     Pageable pageable){
        try{
            List<Student> students;
            if(classIds != null && classIds.length() != 0){
                int[] intIds = convertStringArrayToIntArray(classIds.split(","));
                if(active != null){
                    students = studentRepository.findByClassNumberInAndActiveAndAdmissionYearGreaterThanEqual(intIds,
                            Boolean.valueOf(active),
                            Integer.parseInt(year));
                }
                else{
                    students = studentRepository.findByClassNumberInAndAdmissionYearGreaterThanEqual(intIds,
                            Integer.parseInt(year));
                }
            }
            else if(active != null){
                students = studentRepository.findByActiveAndAdmissionYearGreaterThanEqual(Boolean.parseBoolean(active), Integer.parseInt(year));
            }
            else{
                students = studentRepository.findByAdmissionYearGreaterThanEqual(Integer.parseInt(year));
            }
            if(students == null) students = new ArrayList<>();
            Page<Student> studentsPage = new PageImpl<>(students, pageable, students.size());
            return new ResponseEntity<>(studentsPage, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/students/{id}")
    public Student findById(@PathVariable long id){
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Failed to find student with ID : " + id));
    }

    @PostMapping("/students")
    public ResponseEntity<?> saveStudent(@Valid @RequestBody Student student){
        Student createdStudent = studentRepository.save(student);
        return createdStudent != null ? new ResponseEntity<>(createdStudent, HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @PatchMapping("/students/{id}")
    public ResponseEntity<?> updateClass(@PathVariable Long id, @RequestBody Map<String, Integer> classMap){
        studentRepository.findById(id).map(
                student -> {
                  student.setClassNumber(classMap.get("class"));
                  return studentRepository.save(student);
                });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id){
        studentRepository.findById(id).map(student -> {
            student.setActive(false);
            return studentRepository.save(student);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
