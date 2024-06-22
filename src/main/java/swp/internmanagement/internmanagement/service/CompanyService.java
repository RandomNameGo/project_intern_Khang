package swp.internmanagement.internmanagement.service;

import java.util.List;

import swp.internmanagement.internmanagement.entity.Company;
import swp.internmanagement.internmanagement.payload.request.CreateCompanyRequest;
import swp.internmanagement.internmanagement.payload.request.UpdateCompanyRequest;
import swp.internmanagement.internmanagement.payload.response.GetAllCompanyResponse;

public interface CompanyService {

    boolean checkExistedCompanyAndInsert(CreateCompanyRequest companyRequest);

    String deleteCompany(int companyId);

    String updateCompany(int courseId, UpdateCompanyRequest companyRequest);

    List<Company> getAllCompany();

    GetAllCompanyResponse getAllCompanyResponse(int pageNo, int pageSize);
}
