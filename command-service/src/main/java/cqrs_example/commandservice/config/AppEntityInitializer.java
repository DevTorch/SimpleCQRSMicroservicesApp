package cqrs_example.commandservice.config;

import cqrs_example.commandservice.model.dto.SimpleEntityResponseDTO;
import cqrs_example.commandservice.model.mapper.SimpleMapper;
import cqrs_example.commandservice.repository.SimpleEntityRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppEntityInitializer {

    private final SimpleEntityRepository repository;
    private final SimpleMapper simpleMapper;

    @PostConstruct
    public void init() {
        var entityRequestDTOOne = new SimpleEntityResponseDTO("User One", "one@email.com");
        var entityRequestDTOTwo = new SimpleEntityResponseDTO("User Two", "two@email.com");
        var entityRequestDTOThree = new SimpleEntityResponseDTO("User Three", "three@email.com");

//        repository.save(simpleMapper.toEntity(entityRequestDTOOne));
//        repository.save(simpleMapper.toEntity(entityRequestDTOTwo));
//        repository.save(simpleMapper.toEntity(entityRequestDTOThree));
    }
}
