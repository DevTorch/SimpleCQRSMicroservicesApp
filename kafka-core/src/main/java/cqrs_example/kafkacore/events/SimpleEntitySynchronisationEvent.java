package cqrs_example.kafkacore.events;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record SimpleEntitySynchronisationEvent(
        @NotNull Long id,
        @NotNull String fullName,
        @NotNull @Email String email,
        String description
) implements Serializable {
}
