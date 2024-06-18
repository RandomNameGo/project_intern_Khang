package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.payload.request.CreateCompanyRequest;

public interface CompanyService {

    boolean checkExistedCompanyAndInsert(CreateCompanyRequest companyRequest);

    
}
