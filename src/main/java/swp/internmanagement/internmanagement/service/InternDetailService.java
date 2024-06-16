package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.InternDetail;
import swp.internmanagement.internmanagement.payload.request.UpdateInternDetailRequest;

public interface InternDetailService {
    String updateInternDetail(UpdateInternDetailRequest updateInternDetailRequest, int internId);
}
