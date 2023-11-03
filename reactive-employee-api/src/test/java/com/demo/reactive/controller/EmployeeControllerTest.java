package com.demo.reactive.controller;

import com.demo.reactive.configuration.TestR2dbcConfiguration;
import com.demo.reactive.model.Employee;
import com.demo.reactive.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(EmployeeController.class)
@Import(TestR2dbcConfiguration.class)
public class EmployeeControllerTest {
    @InjectMocks
    private EmployeeController employeeController;

    @MockBean
    private EmployeeService service;

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void saveEmployeeTest() {
        Employee employee = Employee.builder()
                .email("longjipanse@gmail.com")
                .firstName("Longji")
                .lastName("Panse")
                .build();
        when(service.save(employee)).thenReturn(Mono.just(employee));

        String response = webTestClient.post()
                .uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(employee))
                .exchange()
                .expectStatus().isCreated()
                .returnResult(String.class)
                .getResponseBody()
                .blockFirst();


    }

    @Test
    public void saveEmployeeWithErrorEmailTest() {
        Employee employee = Employee.builder()
                .email("try@gmail.com")
                .firstName("John")
                .lastName("Doe")
                .build();
        when(service.save(employee)).thenReturn(Mono.just(employee));

        String response = webTestClient.post()
                .uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(employee))
                .exchange()
                .expectStatus().isBadRequest()
                .returnResult(String.class)
                .getResponseBody()
                .blockFirst();



    }

    @Test
    public void getAllEmployeesTest(){

        // Insert sample data into the in-memory database
        Employee employee1 = Employee.builder()
                .email("Johndoe@gmail.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        Employee employee2 = Employee.builder()
                .email("Joe@gmail.com")
                .firstName("Mr")
                .lastName("Joe")
                .build();

        when(service.getAllEmployees()).thenReturn(Flux.just(employee1, employee2));
        String response = webTestClient.get()
                .uri("/api/employees")
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody()
                .blockFirst();

        assertTrue(response.contains(employee1.getLastName()));
    }

    @Test
    public void updateEmployeeTest() {
        Employee employee = Employee.builder()
                .id(1L)
                .email("johndoe@gmail.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        // Mock the editEmployee method to return a Mono with an updated employee
        Employee updatedEmployee = Employee.builder()
                .id(1L)
                .email("updated@gmail.com")
                .firstName("Updated")
                .lastName("Doe")
                .build();

        when(service.updateEmployee(employee.getId(), employee)).thenReturn(Mono.just(updatedEmployee));

        // Send the PUT request to update the employee
        webTestClient.put()
                .uri("/api/employee/{id}", employee.getId()) // Use "employees" instead of "employee"
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(updatedEmployee))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .returnResult();
    }

    @Test
    public void deleteEmployeeTest() {
        Long employeeId = 1L; // Replace with an existing employee ID

        when(service.deleteEmployee(employeeId)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/employees/{id}", employeeId)
                .exchange()
                .expectStatus().isNoContent();
    }

}
