package com.demo.reactive.repository;


import com.demo.reactive.model.Employee;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface EmployeeRepository extends R2dbcRepository<Employee, Long> {
}
