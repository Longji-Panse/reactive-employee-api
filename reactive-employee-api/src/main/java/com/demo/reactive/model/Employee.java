package com.demo.reactive.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "employee")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Employee {

   @Id
    private long id;

    @NotBlank(message = "First Name cannot be blank.")
    @Column(value = "firstName")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank.")
    @Column(value = "lastName")
    private String lastName;

    @NotBlank(message = "Email address cannot be blank.")
    @Email(message = "Email address must be valid")
    @Column(value = "email")
    private String email;


}
