package cqrs_example.commandservice.service;

import cqrs_example.commandservice.model.dto.SimpleEntityResponseDTO;

public interface SimpleEntityService {
    void save(SimpleEntityResponseDTO entityRequestDTO);
    void delete(Long id);
    void update(Long id, SimpleEntityResponseDTO entityRequestDTO);
}
