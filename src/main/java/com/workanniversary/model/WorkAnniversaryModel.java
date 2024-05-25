package com.workanniversary.model;

        import java.time.LocalDate;

        import lombok.AllArgsConstructor;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;
        import org.springframework.web.multipart.MultipartFile;



        import jakarta.persistence.Column;
        import jakarta.persistence.Entity;
        import jakarta.persistence.GeneratedValue;
        import jakarta.persistence.GenerationType;
        import jakarta.persistence.Id;

        import jakarta.persistence.Table;
        import jakarta.persistence.Transient;


@Entity
@Table(name="work_anniversary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkAnniversaryModel {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="emp_id")
    private Long empId;

    @Column(name="emp_name")
    private String empName;

    @Column(name="emp_designation")
    private String empDesignation;

    @Column(name="emp_Department")
    private String empDepartment;


    @Column(name="img_name")
    private String imgName;

    private String email;

    @Column(name="date_of_joining")
    private LocalDate dateOfJoining;

    @Column(name="filePath")
    private String filePath;


    @Transient
    private MultipartFile file;
}