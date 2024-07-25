package cqrs_example.commandservice.config;

import cqrs_example.commandservice.model.dto.SimpleEntityRequestDTO;
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
        var entityRequestDTOOne = new SimpleEntityRequestDTO("User One", "one@email.com");
        var entityRequestDTOTwo = new SimpleEntityRequestDTO("User Two", "two@email.com");
        var entityRequestDTOThree = new SimpleEntityRequestDTO("User Three", "three@email.com");

//        repository.save(simpleMapper.toEntity(entityRequestDTOOne));
//        repository.save(simpleMapper.toEntity(entityRequestDTOTwo));
//        repository.save(simpleMapper.toEntity(entityRequestDTOThree));
    }
}
