package com.workanniversary.serviceimpl;

import com.workanniversary.email.WorkAnniversaryEmail;
import com.workanniversary.model.WorkAnniversaryModel;
import com.workanniversary.repository.WorkAnniversaryRepository;
import com.workanniversary.service.WorkAnniversaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

@Service
public class WorkAnniversaryServiceImpl implements WorkAnniversaryService {

    private final String storageDirectoryPath = "D:\\workanniversary";

    @Autowired
    private WorkAnniversaryRepository workAnniversaryRepository;

    @Autowired
    private WorkAnniversaryEmail workAnniversaryEmail;




    @Override
    public String uploadEmployee(Long empId, String empName, String empDesignation, String empDepartment,
                                 String email, LocalDate dateOfJoining, String filePath, MultipartFile file) throws IOException {
        // Set file name and path
        String fileName = file.getOriginalFilename();

        // Initialize savedEmp
        WorkAnniversaryModel savedEmp = new WorkAnniversaryModel();

        Path storageDirectory = Paths.get(storageDirectoryPath);
        Path absolutePath = storageDirectory.toAbsolutePath();

        if (!Files.exists(absolutePath)) {
            System.out.println("First time creating directory");
            System.out.println(absolutePath);
            Files.createDirectories(storageDirectory); // Create the directory in the given storage directory path
        }

        Path destination = Paths.get(storageDirectory + "\\" + fileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        final int rootLength = absolutePath.toString().length();
        final String absFileName = destination.toString();
        final String relFileName = absFileName.substring(2);

        // Set employee details
        savedEmp.setEmpId(empId);
        savedEmp.setEmpName(empName);
        savedEmp.setEmpDesignation(empDesignation);
        savedEmp.setEmpDepartment(empDepartment);
        savedEmp.setEmail(email);
        savedEmp.setDateOfJoining(dateOfJoining);
        savedEmp.setImgName(fileName);
        savedEmp.setFilePath(relFileName);

        // Save employee details and image path
        workAnniversaryRepository.save(savedEmp);

        return "Employee details and image path uploaded successfully. Image Path: " + relFileName;
    }


    public String getImagePath(Long empId) {
        Optional<WorkAnniversaryModel> employee = workAnniversaryRepository.findByEmpId(empId);
        if (employee.isPresent()) {
            String imagePath = employee.get().getFilePath();
            return imagePath;
        } else {
            return null;
        }
    }


    public List<WorkAnniversaryModel> getAllEmployee() {
        List<WorkAnniversaryModel> work = workAnniversaryRepository.findAll();
        List<WorkAnniversaryModel> newWork = new ArrayList<>();
        for (WorkAnniversaryModel i : work) {
            try {
                WorkAnniversaryModel workanni = new WorkAnniversaryModel();

                String img = i.getImgName();

                // Construct the file path
                Path filePath = Paths.get(storageDirectoryPath, img);

                // Read the file content into a byte array
                byte[] fileBytes = Files.readAllBytes(filePath);

                // Convert byte array to Base64-encoded string
                String base64Image = Base64.getEncoder().encodeToString(fileBytes);

                // Set the properties in the new WorkAnniversaryModel
                workanni.setId(i.getId());
                workanni.setEmpId(i.getEmpId());
                workanni.setEmpName(i.getEmpName());
                workanni.setEmpDesignation(i.getEmpDesignation());
                workanni.setEmpDepartment(i.getEmpDepartment());
                workanni.setEmail(i.getEmail());
                workanni.setDateOfJoining(i.getDateOfJoining());
                workanni.setImgName(base64Image); // Set the Base64-encoded string
                workanni.setFilePath(i.getFilePath());

                newWork.add(workanni);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        return newWork;
    }



    @Override
    public boolean sentSampleMail(Long empId, String message) {
//        empIdStore.add(empId);
        Optional<WorkAnniversaryModel> workOptional = workAnniversaryRepository.findByEmpId(empId);
        if (workOptional.isPresent()) {
            String to = workOptional.get().getEmail();
            WorkAnniversaryModel workAnniversaryModel=workAnniversaryRepository.findByEmpId(empId).orElse(null);
            String empName=workAnniversaryModel.getEmpName();
            return sentSampleMailBody(to, message,empName);
        }
        return false;
    }




    @Override
    public boolean sentSampleMailBody(String to, String message,String empName) {
        String subject = "Work Anniversary Wish";
        String body = "Dear " + to + ",\n\n" + message;
        try {
            workAnniversaryEmail.sendEmail(to, subject, body, empName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public String sendWishedStatus() {
        return "ok";
    }



}