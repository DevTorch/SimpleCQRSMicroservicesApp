package cqrs_example.commandservice.service.impl;

import cqrs_example.commandservice.exception.SimpleEntityNotFoundException;
import cqrs_example.commandservice.model.dto.SimpleEntityRequestDTO;
import cqrs_example.commandservice.model.mapper.SimpleMapper;
import cqrs_example.commandservice.repository.SimpleEntityRepository;
import cqrs_example.commandservice.service.SimpleEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SimpleEntityServiceImplementation implements SimpleEntityService {

    private final SimpleEntityRepository repository;
    private final SimpleMapper simpleMapper;

    @Override
    public void save(SimpleEntityRequestDTO entityRequestDTO) {
        repository.save(simpleMapper.toEntity(entityRequestDTO));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void update(Long id, SimpleEntityRequestDTO entityRequestDTO) {
        var entity = repository.findById(id).orElseThrow(
                () -> new SimpleEntityNotFoundException(String.format("Filed to update entity with id: %d", id)));
        entity.setEmail(entityRequestDTO.email());
        entity.setFullName(entityRequestDTO.fullName());
        entity.setDescription(String.format("Last Update: %s", Instant.now().toString()));
        repository.save(entity);
    }

}
