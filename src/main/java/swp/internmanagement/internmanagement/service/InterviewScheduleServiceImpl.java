package swp.internmanagement.internmanagement.service;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.entity.JobApplication;
import swp.internmanagement.internmanagement.entity.Schedule;
import swp.internmanagement.internmanagement.payload.request.AddScheduleRequest;
import swp.internmanagement.internmanagement.payload.request.ApplicationIdRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllScheduleOfManager;
import swp.internmanagement.internmanagement.payload.response.GetAllScheduleResponse;
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
            LocalTime workTime = LocalTime.of(9,0);
            LocalTime workTime2 = LocalTime.of(11,30);
            LocalTime workTime3 = LocalTime.of(13,0);
            LocalTime workTime4 = LocalTime.of(17,30);
            JobApplication jobApp=job.get();
            if(interviewDate.isBefore(LocalDateTime.now())) {
              throw new RuntimeException ("The interview date is in the past");
            }

            if(interviewDate.toLocalTime().isBefore(workTime) ||
                    (interviewDate.toLocalTime().isAfter(workTime2)&&interviewDate.toLocalTime().isBefore(workTime3)) ||
                    interviewDate.toLocalTime().isAfter(workTime4)) {
                throw new RuntimeException ("The interview time must be in work time");
            }

            Instant interviewInstant = interviewDate.toInstant(ZoneOffset.UTC);
            schedule.setScheduleTime(interviewInstant);
            JobApplication jobApplication = applicationRepository.findById(a.getApplicationId()).get();
            schedule.setApplication(jobApplication);
            schedule.setLocation(addScheduleRequest.getLocation());
            templateModel.put("date", "Date InterView: " + interviewDate);
            emailService.sendEmailSchedule(email, "Interview Schedule", templateModel);
            jobApp.setStatus(2);
            applicationRepository.save(jobApp);
            scheduleRepository.save(schedule);
        }
        return "Added Success";
    }
    @Override
    public List<GetAllScheduleOfManager> getAllScheduleOfManager(Integer companyId) {
        List<Schedule> listSchedule = scheduleRepository.findScheduleByCompanyId(companyId);
        Map<LocalDateTime, GetAllScheduleOfManager> scheduleMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_DATE_TIME;
        int count =0;
        for (Schedule schedule : listSchedule) {
            LocalDateTime dateStart = LocalDateTime.parse(schedule.getScheduleTime().toString(), isoFormatter);
            LocalDateTime dateEnd = dateStart.plusHours(2);
            count++;
            if (!scheduleMap.containsKey(dateStart)) {
                GetAllScheduleOfManager scheduleOfManager = new GetAllScheduleOfManager();
                scheduleOfManager.setStart(dateStart.format(formatter).toString());
                scheduleOfManager.setEnd(dateEnd.format(formatter).toString());
                scheduleOfManager.setTitle("InterView");
                scheduleOfManager.setDescription("");
                scheduleMap.put(dateStart, scheduleOfManager);
            }
    
            GetAllScheduleOfManager existingSchedule = scheduleMap.get(dateStart);
            String newDescription = existingSchedule.getDescription() + "<p> Intern "+count+":" + schedule.getApplication().getFullName() + "</p>";
            existingSchedule.setDescription(newDescription);
        }
    
        return new ArrayList<>(scheduleMap.values());
    }

    @Override
    public GetAllScheduleResponse getAllSchedule(int companyId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Schedule> schedules = scheduleRepository.findByCompanyId(companyId, pageable);
        List<Schedule> scheduleList = schedules.getContent();
        Map<LocalDateTime, GetAllScheduleOfManager> scheduleMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        int count =0;
        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_DATE_TIME;
        for (Schedule schedule : scheduleList) {
            LocalDateTime dateStart = LocalDateTime.parse(schedule.getScheduleTime().toString(), isoFormatter);
            LocalDateTime dateEnd = dateStart.plusHours(2);
            count++;
            if (!scheduleMap.containsKey(dateStart)) {
                GetAllScheduleOfManager scheduleOfManager = new GetAllScheduleOfManager();
                scheduleOfManager.setStart(dateStart.format(formatter).toString());
                scheduleOfManager.setEnd(dateEnd.format(formatter).toString());
                scheduleOfManager.setTitle("InterView");
                scheduleOfManager.setDescription("");
                scheduleMap.put(dateStart, scheduleOfManager);
            }

            GetAllScheduleOfManager existingSchedule = scheduleMap.get(dateStart);
            String newDescription = existingSchedule.getDescription() + "<p> Intern "+count+":" + schedule.getApplication().getFullName() + "</p>";
            existingSchedule.setDescription(newDescription);
        }
        List<GetAllScheduleOfManager> schedulePage = new ArrayList<>(scheduleMap.values());

        GetAllScheduleResponse getAllScheduleResponse = new GetAllScheduleResponse();
        getAllScheduleResponse.setSchedules(schedulePage);
        getAllScheduleResponse.setPageNo(pageNo);
        getAllScheduleResponse.setPageSize(pageSize);
        getAllScheduleResponse.setTotalItems(schedules.getTotalElements());
        getAllScheduleResponse.setTotalPages(schedules.getTotalPages());
        return getAllScheduleResponse;
    }
}
