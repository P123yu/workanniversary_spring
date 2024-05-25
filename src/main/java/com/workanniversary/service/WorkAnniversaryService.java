package com.workanniversary.service;

import com.workanniversary.model.WorkAnniversaryModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.workanniversary.model.WorkAnniversaryModel;

public interface WorkAnniversaryService {


    String uploadEmployee(Long empId, String empName, String empDesignation, String empDepartment,
                          String email, LocalDate dateOfJoining, String filePath, MultipartFile file) throws IOException;


    String getImagePath(Long empId);


    List<WorkAnniversaryModel> getAllEmployee();



    boolean sentSampleMail(Long empId, String message);


    boolean sentSampleMailBody(String to, String message,String empName);

//    List<Long> sendWishedStatusScheduled();

    String sendWishedStatus();
}