package cqrs_example.queryservice.controller;

import cqrs_example.queryservice.model.dto.ProcessedMessageDto;
import cqrs_example.queryservice.service.events.KafkaMessageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/messages")
public class KafkaMessagesStorageController {

    private final KafkaMessageStorageService messageService;

    @GetMapping("/processed")
    public List<ProcessedMessageDto> getProcessed() {
        return messageService.getProcessed();
    }

    @GetMapping("/processed/paged")
    public Page<ProcessedMessageDto> getProcessedPaged(Pageable pageable) {
        return messageService.getPageableProcessedMessages(pageable);
    }

    @DeleteMapping("/processed")
    public void deleteAllProcessed() {
        messageService.deleteAllProcessed();
    }
}
