package cqrs_example.commandservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import cqrs_example.commandservice.model.dto.SimpleEntityTransferModelDTO;
import cqrs_example.commandservice.service.SimpleEntityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/api/entities")
@RequiredArgsConstructor
public class SimpleEntityRestController {

    private final SimpleEntityService simpleEntityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleEntityTransferModelDTO create(@RequestBody @Valid SimpleEntityTransferModelDTO dto) {
        return simpleEntityService.create(dto);
    }

    @PostMapping("/many")
    @ResponseStatus(HttpStatus.CREATED)
    public List<SimpleEntityTransferModelDTO> createMany(@RequestBody @Valid List<SimpleEntityTransferModelDTO> dtos) {
        return simpleEntityService.createMany(dtos);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SimpleEntityTransferModelDTO patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        return simpleEntityService.patch(id, patchNode);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam @Valid List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        return simpleEntityService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SimpleEntityTransferModelDTO delete(@PathVariable Long id) {
        return simpleEntityService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        simpleEntityService.deleteMany(ids);
    }
}
