package swp.internmanagement.internmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.entity.Company;
import swp.internmanagement.internmanagement.entity.Course;
import swp.internmanagement.internmanagement.entity.Job;
import swp.internmanagement.internmanagement.entity.Task;
import swp.internmanagement.internmanagement.payload.request.CreateCompanyRequest;
import swp.internmanagement.internmanagement.payload.request.UpdateCompanyRequest;
import swp.internmanagement.internmanagement.payload.response.CompanyNameResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllCompanyResponse;
import swp.internmanagement.internmanagement.repository.CompanyRepository;
import swp.internmanagement.internmanagement.repository.CourseRepository;
import swp.internmanagement.internmanagement.repository.JobRepository;
import swp.internmanagement.internmanagement.repository.TaskRepository;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private CourseService courseService;

    @Override
    public boolean checkExistedCompanyAndInsert(CreateCompanyRequest companyRequest) {
        try {
            if (companyRequest.getCompanyDescription() != null && companyRequest.getCompanyName() != null
                    && companyRequest.getLocation() != null) {
                Optional<Company> company = companyRepository.findByCompanyName(companyRequest.getCompanyName());
                if (!company.isPresent()) {
                    Company companyCreate = new Company();
                    companyCreate.setCompanyDescription(companyRequest.getCompanyDescription());
                    companyCreate.setCompanyName(companyRequest.getCompanyName());
                    companyCreate.setLocation(companyRequest.getLocation());
                    companyRepository.save(companyCreate);
                }
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    @Override
    public String deleteCompany(int companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new RuntimeException("Company not found");
        }
        List<Course> courses = courseRepository.findByCompanyId(companyId);
        for (Course course : courses) {
            courseService.deleteCourse(course.getId());
        }
        List<Job> jobs = jobRepository.findListByCompanyId(companyId);
        jobRepository.deleteAll(jobs);
        companyRepository.deleteById(companyId);
        return "Deleted Company";
    }

    @Override
    public String updateCompany(int courseId, UpdateCompanyRequest companyRequest) {
        if (!companyRepository.existsById(courseId)) {
            return "Company not found";
        }
        Company company = companyRepository.findById(courseId).get();
        String newCompanyName = companyRequest.getCompanyName();
        String newLocation = companyRequest.getLocation();
        String newDescription = companyRequest.getDescription();
        if (!newCompanyName.isEmpty()) {
            company.setCompanyName(newCompanyName);
        }
        if (!newLocation.isEmpty()) {
            company.setLocation(newLocation);
        }
        if (!newDescription.isEmpty()) {
            company.setCompanyDescription(newDescription);
        }
        companyRepository.save(company);
        return "Updated Company";
    }

    @Override
    public List<Company> getAllCompany() {
        List<Company> list = companyRepository.findAll();
        return list;
    }

    @Override
    public GetAllCompanyResponse getAllCompanyResponse(int pageNo, int pageSize) {
        try {
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            Page<Company> company = companyRepository.findAll(pageable);
            List<Company> companyList = company.getContent();
            GetAllCompanyResponse getAllCompanyResponse = new GetAllCompanyResponse();
            getAllCompanyResponse.setCompanyList(companyList);
            getAllCompanyResponse.setPageNo(company.getNumber());
            getAllCompanyResponse.setPageSize(company.getSize());
            getAllCompanyResponse.setTotalItems(company.getTotalElements());
            getAllCompanyResponse.setTotalPages(company.getTotalPages());
            return getAllCompanyResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CompanyNameResponse getCompanyName(int companyId) {
        Company company = companyRepository.findById(companyId).get();
        CompanyNameResponse companyNameResponse = new CompanyNameResponse();
        companyNameResponse.setCompanyName(company.getCompanyName());
        return companyNameResponse;
    }
}
