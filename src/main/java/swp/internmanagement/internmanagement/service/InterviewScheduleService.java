package swp.internmanagement.internmanagement.service;

import java.util.List;

import swp.internmanagement.internmanagement.payload.request.AddScheduleRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllScheduleOfManager;

public interface InterviewScheduleService {
    String addSchedule(AddScheduleRequest addScheduleRequest);
    List<GetAllScheduleOfManager> getrAllScheduleOfManager(Integer companyId);
}
