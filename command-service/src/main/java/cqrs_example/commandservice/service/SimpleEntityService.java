package cqrs_example.commandservice.service;

import cqrs_example.commandservice.model.dto.SimpleEntityRequestDTO;

public interface SimpleEntityService {
    void save(SimpleEntityRequestDTO entityRequestDTO);
    void delete(Long id);
    void update(Long id, SimpleEntityRequestDTO entityRequestDTO);
}
