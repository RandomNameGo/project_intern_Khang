package swp.internmanagement.internmanagement.service;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.entity.JobApplication;
import swp.internmanagement.internmanagement.entity.Schedule;
import swp.internmanagement.internmanagement.payload.request.AddScheduleRequest;
import swp.internmanagement.internmanagement.payload.request.ApplicationIdRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllScheduleOfManager;
import swp.internmanagement.internmanagement.repository.JobApplicationRepository;
import swp.internmanagement.internmanagement.repository.ScheduleRepository;

@Service
public class InterviewScheduleServiceImpl implements InterviewScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;
    @Autowired
    private EmailService emailService;
    @Override
    public String addSchedule(AddScheduleRequest addScheduleRequest) {
        List<ApplicationIdRequest> applicationId = addScheduleRequest.getApplicationId();
        Map<String, Object> templateModel = new HashMap<>();

        templateModel.put("verificationCode", "Click it to to change your password");
        for (ApplicationIdRequest a : applicationId) {
            Optional<JobApplication> job=applicationRepository.findById(a.getApplicationId());
            String email =job.get().getEmail();
            System.out.println("Email: "+email);
            Schedule schedule = new Schedule();
            String time = addScheduleRequest.getTime();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
            LocalDateTime interviewDate = LocalDateTime.parse(time, dateTimeFormatter);
            if(interviewDate.isBefore(LocalDateTime.now())) {
              throw new RuntimeException ("The interview date is in the past");
            }
            Instant interviewInstant = interviewDate.toInstant(ZoneOffset.UTC);
            schedule.setScheduleTime(interviewInstant);
            JobApplication jobApplication = applicationRepository.findById(a.getApplicationId()).get();
            schedule.setApplication(jobApplication);
            schedule.setLocation(addScheduleRequest.getLocation());
            templateModel.put("date", "Date InterView: " + interviewDate);
            emailService.sendEmailSchedule(email, "Verify your email", templateModel);
            scheduleRepository.save(schedule);
        }
        return "Added Success";
    }
    @Override
    public List<GetAllScheduleOfManager> getrAllScheduleOfManager(Integer companyId) {
        List<Schedule> listSchedule = scheduleRepository.findScheduleByCompanyId(companyId);
        List<GetAllScheduleOfManager> list =new ArrayList<>();
        for (Schedule schedule : listSchedule) {
            GetAllScheduleOfManager scheduleOfManager = new GetAllScheduleOfManager();
            scheduleOfManager.setDescription("a");
            scheduleOfManager.setEnd(schedule.getScheduleTime().toString());
            scheduleOfManager.setStart(schedule.getScheduleTime().toString());
            scheduleOfManager.setTitle("InterView");
            list.add(scheduleOfManager);
        }
        return list;
    }
}
