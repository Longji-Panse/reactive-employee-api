package com.demo.reactive.service;

import com.demo.reactive.model.Employee;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {
    public Mono<Page<Employee>> getAllEmployeesPaginated(int pageNo, int pageSize);
    public Flux<Employee> getAllEmployees();
    public Mono<Employee> findEmployeeById(long id);
    public Mono<Employee> save(Employee employee);
    public Mono<Employee> updateEmployee(long id, Employee employee);
    public Mono<Void> deleteEmployee(long id);

}
