package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.payload.request.CreateCompanyRequest;
import swp.internmanagement.internmanagement.payload.request.UpdateCompanyRequest;

public interface CompanyService {

    boolean checkExistedCompanyAndInsert(CreateCompanyRequest companyRequest);

    String deleteCompany(int companyId);

    String updateCompany(int courseId, UpdateCompanyRequest companyRequest);
}
