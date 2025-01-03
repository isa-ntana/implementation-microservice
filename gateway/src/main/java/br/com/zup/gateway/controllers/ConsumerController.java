package br.com.zup.gateway.controllers;

import br.com.zup.gateway.infra.clients.consumer.dtos.ConsumerRegisterDTO;
import br.com.zup.gateway.infra.clients.consumer.dtos.ConsumerResponseDTO;
import br.com.zup.gateway.services.ConsumerAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    private static final Logger log = LoggerFactory.getLogger(ConsumerAddressController.class);

    @Autowired
    private ConsumerAddressService consumerAddressService;

    @GetMapping("/all")
    public ResponseEntity<List<ConsumerResponseDTO>> getAllConsumers(){
        try {
            log.info("Start get all consumers and addresses flow");
            List<ConsumerResponseDTO> response = consumerAddressService.getAllConsumers();
            log.info("Finish get all consumers and addresses flow");
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            log.error("Error occurred while fetching consumers: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{consumerId}")
    public ResponseEntity<Void> deleteConsumer(@PathVariable String consumerId) {
        try {
            log.info("Start deleting consumer with ID: {}", consumerId);
            consumerAddressService.deleteConsumerById(consumerId);
            log.info("Finish deleting consumer with ID: {}", consumerId);
            return ResponseEntity.status(204).build();
        } catch (RuntimeException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{consumerId}")
    public ResponseEntity<ConsumerResponseDTO> updateConsumer(
            @PathVariable String consumerId,
            @RequestBody ConsumerRegisterDTO registerDTO) {
        try {
            log.info("Start updating consumer with ID: {}", consumerId);
            ConsumerResponseDTO response = consumerAddressService.updateConsumer(consumerId, registerDTO);
            log.info("Finish updating consumer with ID: {}", consumerId);
            return ResponseEntity.status(200).body(response);
        } catch (RuntimeException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
