package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.InternDetail;
import swp.internmanagement.internmanagement.payload.request.UpdateInternDetailRequest;
import swp.internmanagement.internmanagement.payload.response.InternDetailResponse;
import swp.internmanagement.internmanagement.payload.response.ListInternDetailResponse;
import swp.internmanagement.internmanagement.repository.InternDetailRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class InternDetailServiceImpl implements InternDetailService {

    @Autowired
    private InternDetailRepository internDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String updateInternDetail(UpdateInternDetailRequest updateInternDetailRequest, int internId) {
        if(!userRepository.existsById(internId)) {   //throw exception here
            return "Intern not found";
        }
        InternDetail internDetail = internDetailRepository.findById(internId).get();
        internDetail.setWorkHistory(updateInternDetailRequest.getWorkHistory());
        internDetail.setEducationBackground(updateInternDetailRequest.getEducationBackground());
        internDetailRepository.save(internDetail);
        return "Update intern detail successfully";
    }

    @Override
    public InternDetail getInternDetail(int internId) {
        return internDetailRepository.findByInternId(internId);
    }

    @Override
    public ListInternDetailResponse listInternDetail(int companyId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<InternDetail> internDetailPage = internDetailRepository.findByCompanyId(companyId, pageable);
        List<InternDetail> internDetailList = internDetailPage.getContent();
        List<InternDetailResponse> internDetailResponseList = new ArrayList<>();

        for(InternDetail internDetail : internDetailList) {
            InternDetailResponse internDetailResponse = new InternDetailResponse();
            internDetailResponse.setInternId(internDetail.getUser().getId());
            internDetailResponse.setInternName(internDetail.getUser().getFullName());
            internDetailResponse.setWorkHistory(internDetail.getWorkHistory());
            internDetailResponse.setEducationBackground(internDetail.getEducationBackground());
            internDetailResponseList.add(internDetailResponse);
        }

        ListInternDetailResponse listInternDetailResponse = new ListInternDetailResponse();
        listInternDetailResponse.setInternDetailResponseLists(internDetailResponseList);
        listInternDetailResponse.setPageNo(internDetailPage.getNumber());
        listInternDetailResponse.setPageSize(internDetailPage.getSize());
        listInternDetailResponse.setTotalItems(internDetailPage.getTotalElements());
        listInternDetailResponse.setTotalPages(internDetailPage.getTotalPages());

        return listInternDetailResponse;
    }
}
