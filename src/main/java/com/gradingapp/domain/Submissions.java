package com.gradingapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import com.gradingapp.domain.enumeration.Course;

import com.gradingapp.domain.enumeration.Subject;

import com.gradingapp.domain.enumeration.Exercise;

/**
 * A Submissions.
 */
@Entity
@Table(name = "submissions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Submissions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fdai_number")
    private String fdaiNumber;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "course")
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject")
    private Subject subject;

    @Enumerated(EnumType.STRING)
    @Column(name = "exercises")
    private Exercise exercises;

    @Lob
    @Column(name = "files")
    private byte[] files;

    @Column(name = "files_content_type")
    private String filesContentType;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFdaiNumber() {
        return fdaiNumber;
    }

    public Submissions fdaiNumber(String fdaiNumber) {
        this.fdaiNumber = fdaiNumber;
        return this;
    }

    public void setFdaiNumber(String fdaiNumber) {
        this.fdaiNumber = fdaiNumber;
    }

    public String getName() {
        return name;
    }

    public Submissions name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Course getCourse() {
        return course;
    }

    public Submissions course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Subject getSubject() {
        return subject;
    }

    public Submissions subject(Subject subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Exercise getExercises() {
        return exercises;
    }

    public Submissions exercises(Exercise exercises) {
        this.exercises = exercises;
        return this;
    }

    public void setExercises(Exercise exercises) {
        this.exercises = exercises;
    }

    public byte[] getFiles() {
        return files;
    }

    public Submissions files(byte[] files) {
        this.files = files;
        return this;
    }

    public void setFiles(byte[] files) {
        this.files = files;
    }

    public String getFilesContentType() {
        return filesContentType;
    }

    public Submissions filesContentType(String filesContentType) {
        this.filesContentType = filesContentType;
        return this;
    }

    public void setFilesContentType(String filesContentType) {
        this.filesContentType = filesContentType;
    }

    public User getUser() {
        return user;
    }

    public Submissions user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Submissions submissions = (Submissions) o;
        if (submissions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), submissions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Submissions{" +
            "id=" + getId() +
            ", fdaiNumber='" + getFdaiNumber() + "'" +
            ", name='" + getName() + "'" +
            ", course='" + getCourse() + "'" +
            ", subject='" + getSubject() + "'" +
            ", exercises='" + getExercises() + "'" +
            ", files='" + getFiles() + "'" +
            ", filesContentType='" + getFilesContentType() + "'" +
            "}";
    }
}
