package cqrs_example.commandservice.config;

import cqrs_example.commandservice.model.dto.SimpleEntityTransferModelDTO;
import cqrs_example.commandservice.model.mapper.SimpleMapper;
import cqrs_example.commandservice.repository.SimpleEntityCommandRepository;
import cqrs_example.commandservice.service.SimpleEntityService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AppEntityInitializer {

    private final SimpleEntityService simpleEntityService;
    private final SimpleMapper simpleMapper;

    @Transactional
    @PostConstruct
    public void init() {
        var entityRequestDTOOne = new SimpleEntityTransferModelDTO("User One", "one@email.com");
        var entityRequestDTOTwo = new SimpleEntityTransferModelDTO("User Two", "two@email.com");
        var entityRequestDTOThree = new SimpleEntityTransferModelDTO("User Three", "three@email.com");

        simpleEntityService.create(entityRequestDTOOne);
        simpleEntityService.create(entityRequestDTOTwo);
        simpleEntityService.create(entityRequestDTOThree);

//        repository.save(simpleMapper.toEntity(entityRequestDTOOne));
//        repository.save(simpleMapper.toEntity(entityRequestDTOTwo));
//        repository.save(simpleMapper.toEntity(entityRequestDTOThree));
    }
}
