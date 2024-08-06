package cqrs_example.queryservice.model.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record SimpleEntityTransferModelDTO (

        @Id
        Long id,

        @NotNull(message = "Full Name is required")
        @NotBlank(message = "Full Name is required")
        @Size(min = 3, max = 50)
        String fullName,

        @NotNull(message = "Email is required")
        @NotBlank(message = "Email is required")
        @Email
        String email) implements Serializable {
}
