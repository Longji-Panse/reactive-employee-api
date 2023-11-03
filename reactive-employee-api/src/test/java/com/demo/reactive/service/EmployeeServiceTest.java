package com.demo.reactive.service;

import com.demo.reactive.exceptions.EmployeeNotFoundException;
import com.demo.reactive.model.Employee;
import com.demo.reactive.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@DataR2dbcTest
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUp() {
        // Initialize or reset any necessary configurations before each test.
    }

    @Test
    public void testGetAllEmployees() {
        // Prepare test data
        List<Employee> employees = Arrays.asList(
                new Employee(1, "John", "Doe", "john@example.com"),
                new Employee(2, "Jane", "Smith", "jane@example.com")
        );

        // Mock the repository's findAll method
        when(employeeRepository.findAll()).thenReturn(Flux.fromIterable(employees));

        // Test the service method
        Flux<Employee> result = employeeService.getAllEmployees();

        // Assert the result using StepVerifier
        StepVerifier.create(result)
                .expectNextCount(employees.size())
                .verifyComplete();
    }

    @Test
    public void testFindEmployeeById() {
        // Prepare test data
        long id = 1;
        Employee employee = new Employee(id, "John", "Doe", "john@example.com");

        // Mock the repository's findById method
        when(employeeRepository.findById(id)).thenReturn(Mono.just(employee));

        // Test the service method
        Mono<Employee> result = employeeService.findEmployeeById(id);

        // Assert the result
        StepVerifier.create(result)
                .expectNextMatches(e -> e.getId() == id)
                .verifyComplete();
    }

    @Test
    public void testFindEmployeeByIdNotFound() {
        // Prepare test data
        long id = 1;

        // Mock the repository's findById method to return an empty Mono
        when(employeeRepository.findById(id)).thenReturn(Mono.empty());

        // Test the service method
        Mono<Employee> result = employeeService.findEmployeeById(id);

        // Assert that it throws EmployeeNotFoundException
        StepVerifier.create(result)
                .expectError(EmployeeNotFoundException.class)
                .verify();
    }

    @Test
    public void testSaveEmployee() {
        // Prepare test data
        Employee employee = new Employee(1, "John", "Doe", "john@example.com");

        // Mock the repository's save-method
        when(employeeRepository.save(employee)).thenReturn(Mono.just(employee));

        // Test the service method
        Mono<Employee> result = employeeService.save(employee);

        // Assert the result
        StepVerifier.create(result)
                .expectNextMatches(e -> e.getId() == 1)
                .verifyComplete();
    }

    @Test
    public void testUpdateEmployee() {
        // Prepare test data
        long id = 1;
        Employee employee = new Employee(id, "Updated", "Employee", "updated@example.com");
        Employee existingEmployee = new Employee(id, "John", "Doe", "john@example.com");

        // Mock the repository's findById and save methods
        when(employeeRepository.findById(id)).thenReturn(Mono.just(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(Mono.just(employee));

        // Test the service method
        Mono<Employee> result = employeeService.updateEmployee(id, employee);

        // Assert the result
        StepVerifier.create(result)
                .expectNextMatches(e -> e.getId() == id && e.getFirstName().equals("Updated"))
                .verifyComplete();
    }

    @Test
    public void testUpdateEmployeeNotFound() {
        // Prepare test data
        long id = 1;
        Employee employee = new Employee(id, "Updated", "Employee", "updated@example.com");

        // Mock the repository's findById to return an empty Mono
        when(employeeRepository.findById(id)).thenReturn(Mono.empty());

        // Test the service method
        Mono<Employee> result = employeeService.updateEmployee(id, employee);

        // Assert that it throws EmployeeNotFoundException
        StepVerifier.create(result)
                .expectError(EmployeeNotFoundException.class)
                .verify();
    }

    @Test
    public void testDeleteEmployee() {
        // Prepare test data
        long id = 1;

        // Mock the repository's deleteById method
        when(employeeRepository.deleteById(id)).thenReturn(Mono.empty());

        // Test the service method
        Mono<Void> result = employeeService.deleteEmployee(id);

        // Assert the result
        StepVerifier.create(result)
                .verifyComplete();
    }


}
