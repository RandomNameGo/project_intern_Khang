package swp.internmanagement.internmanagement.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.JobApplication;
import swp.internmanagement.internmanagement.entity.Schedule;
import swp.internmanagement.internmanagement.payload.request.AddScheduleRequest;
import swp.internmanagement.internmanagement.payload.request.ApplicationIdRequest;
import swp.internmanagement.internmanagement.repository.JobApplicationRepository;
import swp.internmanagement.internmanagement.repository.ScheduleRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InterviewScheduleServiceImpl implements InterviewScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Override
    public String addSchedule(AddScheduleRequest addScheduleRequest) {
        List<ApplicationIdRequest> applicationId = addScheduleRequest.getApplicationId();
        for (ApplicationIdRequest a : applicationId) {
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
            scheduleRepository.save(schedule);
        }
        return "Added Success";
    }
}
