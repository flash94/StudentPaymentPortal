package com.nwamara.studentportal.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@Entity
@Table(name = "student")
public class Student implements Serializable{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column (name = "reg_number")
    private String studentRegistrationNumber;

    @NotNull
    @Column (name = "username", nullable = false)
    private String userName;


    @Column (name = "firstname")
    private String firstName;

    @Column (name = "lastname")
    private String lastName;

    @Column (name = "middlename")
    private String middleName;

    @Column (name = "department")
    private String department;

    @NotNull
    @Column (name = "email", nullable = false)
    private String email;

    @NotNull
    @Column (name ="password", nullable = false)
    private String password;

    @Column (name ="is_student")
    private Boolean isStudent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


//    public Student(String userName, String email, Boolean isStudent, String password
//            , LocalDateTime createdAt, LocalDateTime updatedAt) {
//    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "student_enrollments",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))

    public Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Invoice> invoices;

    public Student(String email, String userName, String password, Boolean isStudent) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.isStudent = isStudent;
    }

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate(){
        LocalDateTime now = LocalDateTime.now();
        updatedAt = now;
    }
}
