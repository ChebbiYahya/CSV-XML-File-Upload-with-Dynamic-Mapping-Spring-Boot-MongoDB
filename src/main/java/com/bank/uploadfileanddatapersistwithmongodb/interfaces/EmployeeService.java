package com.bank.uploadfileanddatapersistwithmongodb.interfaces;

import com.bank.uploadfileanddatapersistwithmongodb.DTO.EmployeeDto;
import com.bank.uploadfileanddatapersistwithmongodb.entity.Employee;

import java.util.List;

public interface EmployeeService {

    void saveAll(List<EmployeeDto> employees);
    List<Employee> getAll();
    Employee getEmployeeById(Long id);
    boolean existsById(Long id);
}
