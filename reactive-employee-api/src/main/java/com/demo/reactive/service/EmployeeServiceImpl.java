package com.demo.reactive.service;

import com.demo.reactive.exceptions.EmployeeNotFoundException;
import com.demo.reactive.model.Employee;
import com.demo.reactive.repository.EmployeeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private EmployeeRepository employeeRepository;
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Mono<Page<Employee>> getAllEmployeesPaginated(int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Order.asc("id")); // Define the sorting criteria
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        // Fetch all employees using reactive repository and return as a Mono<Page<Employee>>
        return employeeRepository.findAll(sort)
                .collectList()
                .flatMap(employeeList -> {
                    // Perform pagination manually
                    int start = (int) pageRequest.getOffset();
                    int end = Math.min((start + pageRequest.getPageSize()), employeeList.size());
                    Page<Employee> page = new PageImpl<>(employeeList.subList(start, end), pageRequest, employeeList.size());
                    return Mono.just(page);
                });
    }

    public Flux<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public Mono<Employee> findEmployeeById(long id) {
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException("Employee not found with ID: " + id)));
    }

    @Override
    public Mono<Employee> save(Employee employee) {
        try{
            return employeeRepository.save(employee);
        }catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Employee exists with the email");
        }
    }

    @Override
    public Mono<Employee> updateEmployee(long id, Employee employee) {
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new EmployeeNotFoundException("Employee not found with ID: " + id)))
                .flatMap(existingEmployee -> {
                    // Update the existingEmployee with the new data
                    existingEmployee.setId(id);
                    existingEmployee.setEmail(employee.getEmail());
                    existingEmployee.setFirstName(employee.getFirstName());
                    existingEmployee.setLastName(employee.getLastName());

                    return employeeRepository.save(existingEmployee);
                });
    }

    @Override
    public Mono<Void> deleteEmployee(long id) {
        return employeeRepository.deleteById(id);
    }

}
