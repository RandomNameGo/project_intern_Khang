package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.InternDetail;
import swp.internmanagement.internmanagement.payload.request.UpdateInternDetailRequest;
import swp.internmanagement.internmanagement.payload.response.ListInternDetailResponse;

public interface InternDetailService {
    String updateInternDetail(UpdateInternDetailRequest updateInternDetailRequest, int internId);

    InternDetail getInternDetail(int internId);

    ListInternDetailResponse listInternDetail(int companyId, int pageNo, int pageSize);
}
