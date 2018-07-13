package com.student.management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "students")
public class Student implements Serializable {

    @Id
    @GeneratedValue(generator = "student_id_generator")
    @SequenceGenerator(
            name = "student_id_generator",
            sequenceName = "student_id_sequence",
            initialValue = 100
    )
    private long id;

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @JsonProperty("class")
    @Column(name = "class")
    private int classNumber;

	@JsonIgnore
    @Column(name = "active", columnDefinition = "boolean default true")
    private boolean active;

    @Min(1900)
    @Max(3000)
    @JsonProperty("admissionYear")
    @Column(name = "admission_year")
    private int admissionYear;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    public int getAdmissionYear() {
        return admissionYear;
    }

    public void setAdmissionYear(int admissionYear) {
        this.admissionYear = admissionYear;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
