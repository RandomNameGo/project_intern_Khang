package swp.internmanagement.internmanagement.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import swp.internmanagement.internmanagement.entity.*;
import swp.internmanagement.internmanagement.models.JobApplicationDTO;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.JobApplicationRequest;
import swp.internmanagement.internmanagement.payload.request.PostJobApplicationRequest;
import swp.internmanagement.internmanagement.payload.response.AcceptedJobApplicationResponse;
import swp.internmanagement.internmanagement.payload.response.JobApplicationResponse;
import swp.internmanagement.internmanagement.repository.JobApplicationRepository;
import swp.internmanagement.internmanagement.repository.JobRepository;
import swp.internmanagement.internmanagement.repository.ScheduleRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;
import swp.internmanagement.internmanagement.security.jwt.JwtUtils;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired 
    private UserRepository userRepository;
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public boolean addJobApplication(JobApplicationRequest jobApplicationRequest) {
        Map<String,Object> templateModel = new HashMap<>();
        try {
            JobApplication jobApplication = new JobApplication();
            Job job = new Job();
            UUID verifyCode = UUID.randomUUID();
            job.setId(jobApplicationRequest.getJobId());
            jobApplication.setJob(job);
            templateModel.put("firstName", jobApplicationRequest.getFullName());
            jobApplication.setEmail(jobApplicationRequest.getEmail()+"ANDuniqueCode="+verifyCode.toString());
            jobApplication.setFullName(jobApplicationRequest.getFullName());
            jobApplication.setCV(jobApplicationRequest.getCV().getBytes());
            jobApplication.setStatus(0);
            jobApplicationRepository.save(jobApplication);
            Integer jobApplicationId = jobApplication.getId();
            String code = "jobId=" + jobApplicationRequest.getJobId() +
            "&email=" + jobApplication.getEmail() +
            "&fullName=" + jobApplication.getFullName() +
            "&expire=" + LocalDateTime.now().plusMinutes(5)+
            "&jobApplicationId="+jobApplicationId;
            System.out.println("code: "+code);
            String token = jwtUtils.generateTokenFromUsername(code);
            templateModel.put("verificationLink", "http://localhost:3000/verifyEmail?code="+token);
            emailService.sendVerificationEmail(jobApplicationRequest.getEmail(), "Email Verification", templateModel);
            System.out.println("Job Application ID: " + jobApplicationId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<JobApplication> getJobApplicationById(Integer id) {
        return jobApplicationRepository.findById(id);
    }

    @Override
    public JobApplicationResponse getAllJobApplication(int pageNo, int pageSize, int id) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<JobApplication> jobResponse = jobApplicationRepository.findAllJob(id, 1, pageable);
        List<JobApplication> jobList = jobResponse.getContent();

        List<JobTempo> jobListRes = new ArrayList<>();
        
        for (JobApplication jobApp : jobList) {
            Job job = jobApp.getJob();
            JobTempo jobTempo= new JobTempo(job.getId(), job.getField(), job.getCompany(), job.getJobApplications(), job.getJobName(), job.getJobDescription());
            JobTempo existingJob = null;
            for (JobTempo j : jobListRes) {
                if (j.getId().equals(job.getId())) {
                    existingJob = j;
                    break;
                }
            }
            if (existingJob == null) {
                jobTempo.setJobApplications(new ArrayList<>());
                jobTempo.getJobApplications().add(jobApp);
                jobListRes.add(jobTempo);
            } else {
                existingJob.getJobApplications().add(jobApp);
            }
        }

        JobApplicationResponse jobApplicationResponse = new JobApplicationResponse();
        jobApplicationResponse.setJobList(jobListRes);
        jobApplicationResponse.setPageNo(jobResponse.getNumber());
        jobApplicationResponse.setPageSize(jobResponse.getSize());
        jobApplicationResponse.setTotalItems(jobResponse.getTotalElements());
        jobApplicationResponse.setTotalPages(jobResponse.getTotalPages());

        return jobApplicationResponse;
    }

    @Override
    @Transactional
    public String updateJobApplication(Integer id, int status, Integer userId) throws Exception {
        Optional<JobApplication> jobApplicationOptional = jobApplicationRepository.findById(id);
        UserAccount user =userRepository.findById(userId).get();
        String message = "";
            JobApplication jApplication = jobApplicationOptional.get();
            if(status == 10){
                throw new Exception("You can't update status to pending!");
            }
            message = checkStatus(jApplication,status);
            if(!message.isEmpty() || !message.isBlank()){
                jApplication.setStatus(status);
                jobApplicationRepository.save(jApplication);
                if(status ==3){
                    Schedule schedule = scheduleRepository.findByJobApplicationId(jApplication.getId());
                    scheduleRepository.deleteByJobApplicationId(schedule.getApplication().getId());
                }
                if(status ==0){
                    Map<String, Object> templateModel = new HashMap<>();
                    templateModel.put("applicantName", jApplication.getFullName());
                    templateModel.put("senderName", user.getFullName());
                    templateModel.put("positionName", jApplication.getJob().getJobName());
                    templateModel.put("companyName", jApplication.getJob().getCompany().getCompanyName());
                    templateModel.put("senderPosition", "manager");
                    templateModel.put("contactInformation", jApplication.getJob().getCompany().getLocation()); 
                    emailService.sendEmailReject("anhtdse184413@fpt.edu.vn", "Reject", templateModel);  
                    message = "Reject CV successfully";
                }
            }
                jApplication.setStatus(status);
                jobApplicationRepository.save(jApplication);
                return message;
    }
    public String checkStatus(JobApplication jobApplication, Integer status) throws Exception{
        Integer statusInJob = jobApplication.getStatus();
        String message = "";
        if(statusInJob==null) {
            if(status ==1){
                message = "Accept successfully!";
                return message;
            }
            if(status ==0){
                message="Reject successfully!";
                return message;
            }
            if(status ==2){
                throw new Exception("Can not update pending status to pending interview");
            }
            if(status==3){
                throw new Exception("Can not update pending status to absent status");
            }
            if(status ==4){
                throw new Exception("Can not update pending status to passed status");
            }
            if(status ==5){
                throw new Exception("Can not update pending status to pending rescheduling");
            }
        }
        if(status.equals(statusInJob)){
            message ="Update successfully";
            return message;
        }
        if(statusInJob.equals(4)){
            if(status ==3){
                throw new Exception("Can not update passed status to absent");
            }else if(status ==2){
                throw new Exception("Can not update passed status to pending");
            }else if(status ==1){
                throw new Exception("Can not update passed status to accept");
            }
            else if(status==5){
                throw new Exception("Can not update passed status to Pending Rescheduling");
            }
        }
        if(statusInJob.equals(3)){
            if(status != 0){
                throw new Exception("Please contact to Internship Coordinator to set interview schedule!");
            }
            message = "Reject CV successfully";
            return message;
        }
        if(statusInJob.equals(2)){
            if(status == 1){
               throw new Exception("You can't not update status to Accept!");
            }
            if(status == 3){
                message ="Update absent status successfully!";
                return message;
            }
            if(status == 4){
                message = "Update Passed status successfully!";
                return message;
            }
            if(status ==0){
                message ="Reject successfully";
            }
            if(status==5){
                throw new Exception("Can not update passed status to Pending Rescheduling");
            }
        }
        if(statusInJob.equals(1)){
           if(status==0||status ==2 || status ==3 || status ==4 || status ==5){
                throw new Exception("Error to change status!!!");
           }
        }
        if(statusInJob.equals(5)){
            if(status != 0){
                throw new Exception("Please contact to Internship Coordinator to set interview schedule!!!!!!");
            }
            message = "Reject CV successfully";
            return message;
        }
        
        return message;
    
    }

    @Override
    public AcceptedJobApplicationResponse getAcceptedJobApplication(int pageNo, int pageSize) {
        // Pageable pageable = PageRequest.of(pageNo, pageSize);
        // Page<JobApplication> jobApplications = jobApplicationRepository.findAcceptedJobApplications(pageable);
        // List<JobApplication> jobApplicationList = jobApplications.getContent();

        // AcceptedJobApplicationResponse response = new AcceptedJobApplicationResponse();
        // response.setJobApplications(jobApplicationList);
        // response.setPageSize(jobApplications.getSize());
        // response.setPageNo(jobApplications.getNumber());
        // response.setTotalItems(jobApplications.getTotalElements());
        // response.setTotalPages(jobApplications.getTotalPages());

        // return response;
        return null;
    }

    @Override
    public AcceptedJobApplicationResponse getAllAcceptedJobApplication(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<JobApplication> jobApplicationPage = jobApplicationRepository.findByStatus(1, pageable);
        
        List<JobApplicationDTO> jobApplicationDTOs = jobApplicationPage.getContent().stream()
                .map(ja -> new JobApplicationDTO(
                        ja.getId(),
                        ja.getFullName(),
                        ja.getEmail(),
                        ja.getJob().getCompany().getId(),
                        ja.getJob().getCompany().getCompanyName(),
                        ja.getStatus()
                ))
                .collect(Collectors.toList());

        AcceptedJobApplicationResponse response = new AcceptedJobApplicationResponse();
        response.setJobApplications(jobApplicationDTOs);
        response.setPageSize(jobApplicationPage.getSize());
        response.setPageNo(jobApplicationPage.getNumber());
        response.setTotalItems(jobApplicationPage.getTotalElements());
        response.setTotalPages(jobApplicationPage.getTotalPages());
        
        return response;
    }
    @Override
    public boolean postJobApplication(PostJobApplicationRequest postJobApplicationRequest) {
        try {
            Job job = new Job();
            Company company =new Company();
            company.setId(postJobApplicationRequest.getCompany_id());
            Field field = new Field();
            field.setId(postJobApplicationRequest.getField_id());
            job.setCompany(company);
            job.setField(field);
            job.setJobDescription(postJobApplicationRequest.getJob_description());
            job.setJobName(postJobApplicationRequest.getJob_name());
            jobRepository.save(job);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public AcceptedJobApplicationResponse getAllAcceptedJobApplicationById(Integer companyId, int pageNo,
            int pageSize) {
                Pageable pageable = PageRequest.of(pageNo, pageSize);
                Page<JobApplication> jobApplicationPage = jobApplicationRepository.findByJobCompanyIdAndStatusAndSchedulesIsNull(companyId, 1, pageable);
                
                List<JobApplicationDTO> jobApplicationDTOs = jobApplicationPage.getContent().stream()
                        .map(ja -> new JobApplicationDTO(
                                ja.getId(),
                                ja.getFullName(),
                                ja.getEmail(),
                                ja.getJob().getCompany().getId(),
                                ja.getJob().getCompany().getCompanyName(),
                                ja.getStatus()
                        ))
                        .collect(Collectors.toList());
        
                AcceptedJobApplicationResponse response = new AcceptedJobApplicationResponse();
                response.setJobApplications(jobApplicationDTOs);
                response.setPageSize(jobApplicationPage.getSize());
                response.setPageNo(jobApplicationPage.getNumber());
                response.setTotalItems(jobApplicationPage.getTotalElements());
                response.setTotalPages(jobApplicationPage.getTotalPages());
                
                return response;
    }
    @Override
    public boolean handleVerifyEmailJob(String code) {
            try {
            String result = jwtUtils.getUserNameFromJwtToken(code);
            // String jobId = "jobId=([^&]+)";
            String email = "email=([^&]+)";
            // String fullName = "fullName=([^&]+)";
            String jobApplicationId ="jobApplicationId=([^&]+)";
            String time = "expire=([^&]+)";

            LocalDateTime timeAfter=LocalDateTime.parse(extractValue(result, time));
            String emailAfter = extractValue(result, email);
            Integer jobApplicationIdAfter=Integer.parseInt(extractValue(result, jobApplicationId));
            if(timeAfter.isBefore(LocalDateTime.now())){
                throw new Exception("Out of time");
            }
            JobApplication jobApplication = jobApplicationRepository.findById(jobApplicationIdAfter).get();
            if(jobApplication.getEmail().equals(emailAfter)){
                String emailtemp = emailAfter.split("AND")[0];
                // String uniqueCode = emailAfter.split("uniqueCode=")[1];
                jobApplication.setEmail(emailtemp);
                jobApplication.setStatus(null);
                jobApplicationRepository.save(jobApplication);
            }else{
                throw new Exception();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
        private static String extractValue(String input, String pattern) {
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateCourseStatus() {
        LocalDate today = LocalDate.now();
        List<JobApplication> jobApplications = jobApplicationRepository.findAll();
        for (JobApplication jobApplication : jobApplications) {
            if(jobApplication.getStatus() == 0){
                jobApplicationRepository.delete(jobApplication);
            }
        }
    }
}
