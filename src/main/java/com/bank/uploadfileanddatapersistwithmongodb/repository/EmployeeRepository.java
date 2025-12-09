package com.bank.uploadfileanddatapersistwithmongodb.repository;


import com.bank.uploadfileanddatapersistwithmongodb.entity.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, Long> {
}
