package com.workanniversary.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.workanniversary.model.WorkAnniversaryModel;

public interface WorkAnniversaryRepository extends JpaRepository<WorkAnniversaryModel,Long> {
    Optional<WorkAnniversaryModel>findByImgName(String imgName);
    Optional<WorkAnniversaryModel>findByEmpId(Long empId);

}
