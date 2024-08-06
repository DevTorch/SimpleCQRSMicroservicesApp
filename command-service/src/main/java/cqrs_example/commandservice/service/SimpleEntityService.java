package cqrs_example.commandservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import cqrs_example.commandservice.model.dto.SimpleEntityTransferModelDTO;

import java.io.IOException;
import java.util.List;

public interface SimpleEntityService {

    SimpleEntityTransferModelDTO create(SimpleEntityTransferModelDTO dto);

    List<SimpleEntityTransferModelDTO> createMany(List<SimpleEntityTransferModelDTO> dtos);

    SimpleEntityTransferModelDTO delete(Long id);

    SimpleEntityTransferModelDTO patch(Long id, JsonNode patchNode) throws IOException;

    List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException;

    void deleteMany(List<Long> ids);
}
