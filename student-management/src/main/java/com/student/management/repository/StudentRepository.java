package com.student.management.repository;

import com.student.management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{

    List<Student> findByClassNumberInAndAdmissionYearGreaterThanEqual(int[] classNumbers, int admissionDate);

    List<Student> findByAdmissionYearGreaterThanEqual(int admissionDate);

    List<Student> findByActiveAndAdmissionYearGreaterThanEqual(boolean active, int admissionDate);

    List<Student> findByClassNumberInAndActiveAndAdmissionYearGreaterThanEqual(int[] classNumber, boolean active, int admissionDate);

}
