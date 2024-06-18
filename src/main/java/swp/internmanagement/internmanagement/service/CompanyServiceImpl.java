package swp.internmanagement.internmanagement.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import swp.internmanagement.internmanagement.entity.Company;
import swp.internmanagement.internmanagement.payload.request.CreateCompanyRequest;
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
                    Company companyCreate= new Company();
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
}
