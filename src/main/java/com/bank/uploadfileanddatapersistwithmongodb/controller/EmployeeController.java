package com.bank.uploadfileanddatapersistwithmongodb.controller;

import com.bank.uploadfileanddatapersistwithmongodb.DTO.EmployeeDto;
import com.bank.uploadfileanddatapersistwithmongodb.entity.Employee;
import com.bank.uploadfileanddatapersistwithmongodb.interfaces.EmployeeService;
import com.bank.uploadfileanddatapersistwithmongodb.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;


    @GetMapping
    public List<EmployeeDto> GetAllEmployees() {
        List<Employee> employees = employeeService.getAll();
        return employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EmployeeDto GetEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return employeeMapper.toDto(employee);
    }
}
