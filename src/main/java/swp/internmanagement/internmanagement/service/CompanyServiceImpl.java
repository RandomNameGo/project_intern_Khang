package swp.internmanagement.internmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.entity.Company;
import swp.internmanagement.internmanagement.payload.request.CreateCompanyRequest;
import swp.internmanagement.internmanagement.payload.request.UpdateCompanyRequest;
import swp.internmanagement.internmanagement.payload.response.CompanyResponse;
import swp.internmanagement.internmanagement.payload.response.GetAllCompanyResponse;
import swp.internmanagement.internmanagement.repository.CompanyRepository;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public boolean checkExistedCompanyAndInsert(CreateCompanyRequest companyRequest) {
        try {
            if (companyRequest.getCompanyDiscription() != null && companyRequest.getCompanyName() != null
                    && companyRequest.getLocation() != null) {
                Optional<Company> company = companyRepository.findByCompanyName(companyRequest.getCompanyName());
                if (!company.isPresent()) {
                    Company companyCreate = new Company();
                    companyCreate.setCompanyDescription(companyRequest.getCompanyDiscription());
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
            return "Company not found";
        }
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
}
