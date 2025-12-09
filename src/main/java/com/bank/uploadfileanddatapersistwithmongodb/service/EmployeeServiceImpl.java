package com.bank.uploadfileanddatapersistwithmongodb.service;


import com.bank.uploadfileanddatapersistwithmongodb.DTO.EmployeeDto;
import com.bank.uploadfileanddatapersistwithmongodb.entity.Employee;
import com.bank.uploadfileanddatapersistwithmongodb.exception.EmployeeNotFoundException;
import com.bank.uploadfileanddatapersistwithmongodb.mapper.EmployeeMapper;
import com.bank.uploadfileanddatapersistwithmongodb.repository.EmployeeRepository;
import com.bank.uploadfileanddatapersistwithmongodb.interfaces.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;




    @Override
    public void saveAll(List<EmployeeDto> employees) {
        List<Employee> entities = employees.stream()
                .map(mapper::toEntity)
                .toList();
        repository.saveAll(entities);
    }

    @Override
    public List<Employee> getAll() {
        return repository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return repository.findById(id).orElseThrow(()-> new EmployeeNotFoundException("Employee not found with id: " + id));
    }
}
