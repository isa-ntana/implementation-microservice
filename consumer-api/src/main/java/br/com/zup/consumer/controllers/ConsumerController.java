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
    public ResponseEntity<ConsumerResponseDTO> createConsumer(@RequestBody ConsumerRegisterDTO registerDTO) {
        try {
            log.info("Start consumer register flow");
            Consumer consumer = consumerService.createConsumer(registerDTO.toEntity());
            log.info("Finish consumer register flow");
            return ResponseEntity.status(201).body(ConsumerResponseDTO.fromEntity(consumer));
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return createErrorResponseDTO(e.getMessage(), 400);
        } catch (RuntimeException e) {
            log.error("Error occurred while creating consumer: {}", e.getMessage());
            return createErrorResponseDTO("An error occurred while creating the consumer: " + e.getMessage(), 500);
        }
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
        return ResponseEntity.status(200).body(response);
    }

    // Read (Get by ID)
    @GetMapping("/{id}")
    public ResponseEntity<Object> getConsumerById(@PathVariable String id) {
        try {
            log.info("Start get consumer by id flow");

            Consumer consumer = consumerService.getConsumerById(id)
                    .orElseThrow(() -> new RuntimeException("Consumer not found with id: " + id));

            log.info("Finish get consumer by id flow");
            return ResponseEntity.status(200).body(ConsumerResponseDTO.fromEntity(consumer));

        } catch (RuntimeException e) {
            log.error("Error occurred while fetching consumer by id: {}", e.getMessage());
            return ResponseEntity.status(404).body("Consumer not found with id: " + id);
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }


    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ConsumerResponseDTO> updateConsumer(@PathVariable String id, @RequestBody ConsumerRegisterDTO consumerRegisterDTO) {
        try {
            log.info("Start update consumer by id flow");
            Consumer updatedConsumer = consumerService.updateConsumer(id, consumerRegisterDTO.toEntity());
            log.info("Finish update consumer by id flow");
            return ResponseEntity.status(200).body(ConsumerResponseDTO.fromEntity(updatedConsumer));
        } catch (RuntimeException e) {
            log.error("Error occurred while creating address: {}", e.getMessage());
            return createErrorResponseDTO("An error occurred while updating the address: " + e.getMessage(), 500);
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            return createErrorResponseDTO("An unexpected error occurred: " + e.getMessage(), 500);
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsumer(@PathVariable String id) {
        try {
            log.info("Start delete consumer by id flow");
            consumerService.deleteConsumer(id);
            log.info("Finish delete consumer by id flow");
            return ResponseEntity.status(204).build();
        } catch (RuntimeException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    private ResponseEntity<ConsumerResponseDTO> createErrorResponseDTO(String message, int statusCode) {
        ConsumerResponseDTO errorResponse = new ConsumerResponseDTO();
        errorResponse.setName("Error: " + message);
        return ResponseEntity.status(statusCode).body(errorResponse);
    }
}