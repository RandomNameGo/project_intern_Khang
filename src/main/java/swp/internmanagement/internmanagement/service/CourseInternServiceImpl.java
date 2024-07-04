package swp.internmanagement.internmanagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.entity.*;
import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.payload.request.AddInternToCourseRequest;
import swp.internmanagement.internmanagement.payload.response.*;
import swp.internmanagement.internmanagement.repository.*;

@Service
public class CourseInternServiceImpl implements CourseInternService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseInternRepository courseInternRepository;

    @Autowired
    private InternTaskService internTaskService;

    @Autowired
    private InternTaskRepository internTaskRepository;

    @Autowired
    private CourseFeedbackRepository courseFeedbackRepository;

    //This method is used by coordinator to add interns to course
    @Override
    public String addInternToCourse(List<AddInternToCourseRequest> request, int courseId) {
        CourseIntern courseIntern = new CourseIntern();
        if(!courseRepository.existsById(courseId)){
            throw new RuntimeException("Course not found");
        }
        Course course = courseRepository.findById(courseId).get();
        List<Task> tasks = course.getTasks();
        LocalDate localDate = LocalDate.now();
        if(course.getStartDate().isBefore(localDate)){
            throw new RuntimeException( "Course already started");
        }

        for (AddInternToCourseRequest addInternToCourseRequest : request) {
            CourseIntern courseInterns=courseInternRepository.findByInternIdAndCourseId(addInternToCourseRequest.getInternId(), courseId);
            if(courseInterns!=null){
                throw new RuntimeException( "There are some intern had been in that course");
            }
        }
        CourseInternId courseInternId = new CourseInternId();
        courseIntern.setCourse(course);
        courseInternId.setCourseId(courseId);
        for (AddInternToCourseRequest a : request) {
            UserAccount userAccount = userRepository.findById(a.getInternId()).get();
            courseIntern.setIntern(userAccount);
            courseInternId.setInternId(a.getInternId());
            courseIntern.setId(courseInternId);
            courseIntern.setResult(0.0);
            courseInternRepository.save(courseIntern);
            for (Task task : tasks) {
                internTaskService.addInternToTask(task);
            }
        }
        return "Added successfully";
    }

    @Override
    public List<CourseIntern> geCoursesByInternId(int internId) {
        return courseInternRepository.findByInternId(internId);
    }

    @Override
    public void updateResult(int internId, int courseId) {
        CourseInternId courseInternId = new CourseInternId();
        courseInternId.setCourseId(courseId);
        courseInternId.setInternId(internId);
        if(!courseInternRepository.existsById(courseInternId)){
            return;
        }
        CourseIntern courseIntern = courseInternRepository.findById(courseInternId).get();
        double result = internTaskService.calculateTotalInternTaskResult(internId, courseId);
        courseIntern.setResult(result);
        courseInternRepository.save(courseIntern);
    }

    @Override
    public GetListInternResultFromCourseResponse getListInternResultFromCourse(int courseId, int mentorId) {

        if(!courseRepository.existsById(courseId)){
            throw new RuntimeException("Course not found");
        }

        Course course = courseRepository.findById(courseId).get();
        if(course.getMentor().getId() != mentorId){
            throw new RuntimeException("you are not allowed to access this course");
        }

        List<CourseIntern> courseInterns = courseInternRepository.findByCourseId(courseId);
        List<GetInternResultFromCourseResponse> result = new ArrayList<>();
        for (CourseIntern courseIntern : courseInterns) {
            GetInternResultFromCourseResponse getInternResultFromCourseResponse = new GetInternResultFromCourseResponse();
            getInternResultFromCourseResponse.setInternId(courseIntern.getIntern().getId());
            getInternResultFromCourseResponse.setInternName(courseIntern.getIntern().getFullName());
            getInternResultFromCourseResponse.setTotalTask(internTaskRepository.countInternTasksByInternId(courseIntern.getIntern().getId(), courseIntern.getCourse().getId()));
            getInternResultFromCourseResponse.setCompletedTasks(internTaskRepository.countInternTasksCompletedByInternId(courseIntern.getIntern().getId(), courseIntern.getCourse().getId()));
            getInternResultFromCourseResponse.setResult(courseIntern.getResult());
            result.add(getInternResultFromCourseResponse);
        }

        GetListInternResultFromCourseResponse getListInternResultFromCourseResponse = new GetListInternResultFromCourseResponse();
        getListInternResultFromCourseResponse.setGetListInternResultFromCourse(result);

        return getListInternResultFromCourseResponse;
    }

    @Override
    public GetAllInternInCourseResponse getAllInternInCourse(int courseId, int mentorId) {
        if(!courseRepository.existsById(courseId)){
            throw new RuntimeException("Course not found");
        }
        Course course = courseRepository.findById(courseId).get();
        if(course.getMentor().getId() != mentorId){
            throw new RuntimeException("you are not allowed to access this course");
        }
        List<CourseIntern> courseInterns = courseInternRepository.findByCourseId(courseId);
        List<InternResponse> internResponseList = new ArrayList<>();
        for (CourseIntern courseIntern : courseInterns) {
            InternResponse internResponse = new InternResponse();
            internResponse.setInternId(courseIntern.getIntern().getId());
            internResponse.setInternName(courseIntern.getIntern().getFullName());
            internResponse.setEmail(courseIntern.getIntern().getEmail());
            internResponseList.add(internResponse);
        }

        GetAllInternInCourseResponse getAllInternInCourseResponse = new GetAllInternInCourseResponse();
        getAllInternInCourseResponse.setInternResponseList(internResponseList);

        return getAllInternInCourseResponse;
    }

    @Override
    public GetAllInternInCourseResponse getAllInternInCourseByCoordinator(int courseId, int coordinatorId) {
        if(!courseRepository.existsById(courseId)){
            throw new RuntimeException("Course not found");
        }
        Course course = courseRepository.findById(courseId).get();
        UserAccount coordinator = userRepository.findById(coordinatorId).get();
        if(!Objects.equals(course.getCompany().getId(), coordinator.getCompany().getId())){
            throw new RuntimeException("you are not allowed to access this course");
        }

        List<CourseIntern> courseInterns = courseInternRepository.findByEndCourseId(courseId);
        List<InternResponse> internResponseList = new ArrayList<>();
        for (CourseIntern courseIntern : courseInterns) {
            InternResponse internResponse = new InternResponse();
            internResponse.setInternId(courseIntern.getIntern().getId());
            internResponse.setInternName(courseIntern.getIntern().getFullName());
            internResponse.setEmail(courseIntern.getIntern().getEmail());
            internResponseList.add(internResponse);
        }

        GetAllInternInCourseResponse getAllInternInCourseResponse = new GetAllInternInCourseResponse();
        getAllInternInCourseResponse.setInternResponseList(internResponseList);

        return getAllInternInCourseResponse;
    }

    @Override
    public Boolean verifyCourseIntern(int internId, int courseId) {
        CourseInternId courseInternId = new CourseInternId();
        courseInternId.setCourseId(courseId);
        courseInternId.setInternId(internId);
        return courseInternRepository.existsById(courseInternId);
    }

    @Override
    public ListEndedCourseByInternResponse getListEndedCourseByIntern(int internId) {
        List<CourseIntern> courseInterns = courseInternRepository.findEndCoursesByInternId(internId);
        List<EndedCourseByInternResponse> endedCourseByInternResponseList = new ArrayList<>();
        CourseFeedbackId courseFeedbackId = new CourseFeedbackId();
        for (CourseIntern courseIntern : courseInterns) {
            EndedCourseByInternResponse endedCourseByInternResponse = new EndedCourseByInternResponse();
            courseFeedbackId.setCourseId(courseIntern.getCourse().getId());
            courseFeedbackId.setInternId(courseIntern.getIntern().getId());
            endedCourseByInternResponse.setCompanyId(courseIntern.getCourse().getCompany().getId());
            endedCourseByInternResponse.setCompanyName(courseIntern.getCourse().getCompany().getCompanyName());
            endedCourseByInternResponse.setCourseId(courseIntern.getCourse().getId());
            endedCourseByInternResponse.setCourseName(courseIntern.getCourse().getCourseDescription());
            endedCourseByInternResponse.setMentorId(courseIntern.getCourse().getMentor().getId());
            endedCourseByInternResponse.setMentorName(courseIntern.getCourse().getMentor().getFullName());
            endedCourseByInternResponse.setStartDate(courseIntern.getCourse().getStartDate());
            endedCourseByInternResponse.setEndDate(courseIntern.getCourse().getEndDate());
            endedCourseByInternResponse.setStatus(courseIntern.getCourse().getStatus());
            endedCourseByInternResponse.setFeedback(courseFeedbackRepository.existsById(courseFeedbackId));
            endedCourseByInternResponseList.add(endedCourseByInternResponse);
        }

        ListEndedCourseByInternResponse listEndedCourseByInternResponse = new ListEndedCourseByInternResponse();
        listEndedCourseByInternResponse.setEndedCourseByInternResponse(endedCourseByInternResponseList);
        return listEndedCourseByInternResponse;
    }
}
