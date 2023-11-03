package com.demo.reactive.controller;

import com.demo.reactive.model.Employee;
import com.demo.reactive.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees/pages")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Page<Employee>> getPaginatedEmployees(

            @RequestParam(value = "pageNo", defaultValue = "0",required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5",required = false) int pageSize
    ){
        return employeeService.getAllEmployeesPaginated(pageNo, pageSize);
    }

    @GetMapping("/employees")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping(value = "/employees/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Employee> getEmployeeById(@PathVariable long id){
        return employeeService.findEmployeeById(id);
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Employee> createEmployee(@Valid @RequestBody Employee employee){
        return employeeService.save(employee);
    }

    @PutMapping("/employee/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Employee> updateEmployee(@PathVariable("id") long id, @Valid @RequestBody Employee employee){
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("employees/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEmployee(@PathVariable("id") Long id){
        return employeeService.deleteEmployee(id);
    }


}
