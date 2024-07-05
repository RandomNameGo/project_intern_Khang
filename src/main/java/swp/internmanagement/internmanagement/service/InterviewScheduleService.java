package swp.internmanagement.internmanagement.service;

import java.util.List;

import swp.internmanagement.internmanagement.payload.request.AddScheduleRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllScheduleOfManager;
import swp.internmanagement.internmanagement.payload.response.GetAllScheduleResponse;

public interface InterviewScheduleService {
    String addSchedule(AddScheduleRequest addScheduleRequest);

    List<GetAllScheduleOfManager> getAllScheduleOfManager(Integer companyId);

    GetAllScheduleResponse getAllSchedule(int companyId, int pageNo, int pageSize);

    String deleteSchedule(int scheduleId);
}
