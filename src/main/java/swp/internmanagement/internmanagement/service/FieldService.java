package swp.internmanagement.internmanagement.service;

import java.util.List;

import swp.internmanagement.internmanagement.entity.Field;
import swp.internmanagement.internmanagement.payload.response.GetAllFieldsResponse;

public interface FieldService {
    GetAllFieldsResponse getAllFields(int pageNo, int pageSize);
    List<Field>getField();
}
