package br.com.zup.consumer.controllers;

import br.com.zup.consumer.controllers.dtos.ConsumerRegisterDTO;
import br.com.zup.consumer.controllers.dtos.ConsumerResponseDTO;
import br.com.zup.consumer.models.Consumer;
import br.com.zup.consumer.services.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    private static final Logger log = LoggerFactory.getLogger(ConsumerController.class);

    @Autowired
    private ConsumerService consumerService;

    // Create
    @PostMapping
    public ResponseEntity<ConsumerResponseDTO> createConsumer(@RequestBody ConsumerRegisterDTO consumerRegisterDTO) {
        log.info("Start consumer register flow");
        Consumer consumer = consumerService.createConsumer(consumerRegisterDTO.toEntity());
        log.info("Finish consumer register flow");
        return ResponseEntity.status(201).body(ConsumerResponseDTO.fromEntity(consumer));
    }

    // Read (Get all)
    @GetMapping
    public ResponseEntity<List<ConsumerResponseDTO>> getAllConsumers() {
        log.info("Start get all consumers flow");
        List<Consumer> consumers = consumerService.getAllConsumers();
        List<ConsumerResponseDTO> response = consumers.stream()
                .map(ConsumerResponseDTO::fromEntity)
                .collect(Collectors.toList());

        log.info("Finish get all consumers flow");
        return ResponseEntity.ok(response);
    }

    // Read (Get by ID)
    @GetMapping("/{id}")
    public ResponseEntity<ConsumerResponseDTO> getConsumerById(@PathVariable String id) {
        log.info("Start get consumer by id flow");
        Consumer consumer = consumerService.getConsumerById(id)
                .orElseThrow(() -> new RuntimeException("Consumer not found with id: " + id));
        log.info("Finish get consumer by id flow");
        return ResponseEntity.ok(ConsumerResponseDTO.fromEntity(consumer));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ConsumerResponseDTO> updateConsumer(@PathVariable String id, @RequestBody ConsumerRegisterDTO consumerRegisterDTO) {
        log.info("Start update consumer by id flow");
        Consumer updatedConsumer = consumerService.updateConsumer(id, consumerRegisterDTO.toEntity());
        log.info("Finish update consumer by id flow");
        return ResponseEntity.ok(ConsumerResponseDTO.fromEntity(updatedConsumer));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsumer(@PathVariable String id) {
        log.info("Start delete consumer by id flow");
        consumerService.deleteConsumer(id);
        log.info("Finish delete consumer by id flow");
        return ResponseEntity.noContent().build();
    }
}