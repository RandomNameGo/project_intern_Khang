package swp.internmanagement.internmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import swp.internmanagement.internmanagement.entity.Field;
import swp.internmanagement.internmanagement.payload.response.GetAllFieldsResponse;
import swp.internmanagement.internmanagement.repository.FieldRepository;

import java.util.List;


@Service
public class FieldServiceImpl implements FieldService {

    @Autowired
    private FieldRepository fieldRepository;

    @Override
    public GetAllFieldsResponse getAllFields(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Field> fields = fieldRepository.findAll(pageable);
        List<Field> fieldList = fields.getContent();

        GetAllFieldsResponse response = new GetAllFieldsResponse();
        response.setFields(fieldList);
        response.setPageNo(fields.getNumber());
        response.setPageSize(fields.getSize());
        response.setTotalItems(fields.getTotalElements());
        response.setTotalPages(fields.getTotalPages());

        return response;
    }
    @Override
    public List<Field> getField() {
        return fieldRepository.findAll();
    }
}
