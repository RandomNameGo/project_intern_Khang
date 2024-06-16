package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.InternDetail;
import swp.internmanagement.internmanagement.payload.request.UpdateInternDetailRequest;
import swp.internmanagement.internmanagement.repository.InternDetailRepository;
import swp.internmanagement.internmanagement.repository.UserRepository;

@Service
public class InternDetailServiceImpl implements InternDetailService {

    @Autowired
    private InternDetailRepository internDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String updateInternDetail(UpdateInternDetailRequest updateInternDetailRequest, int internId) {
        if(!userRepository.existsById(internId)) {
            return "Intern not found";
        }
        InternDetail internDetail = internDetailRepository.findById(internId).get();
        internDetail.setWorkHistory(updateInternDetailRequest.getWorkHistory());
        internDetail.setEducationBackground(updateInternDetailRequest.getEducationBackground());
        internDetailRepository.save(internDetail);
        return "Update intern detail successfully";
    }
}
