package cqrs_example.commandservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import cqrs_example.commandservice.model.dto.SimpleEntityTransferModelDTO;

import java.io.IOException;
import java.util.List;

public interface SimpleEntityService {

    SimpleEntityTransferModelDTO delete(Long id);

    void update(Long id, SimpleEntityTransferModelDTO entityRequestDTO);

    SimpleEntityTransferModelDTO create(SimpleEntityTransferModelDTO dto);

    SimpleEntityTransferModelDTO patch(Long id, JsonNode patchNode) throws IOException;

    List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException;

    void deleteMany(List<Long> ids);
}
