package br.com.zup.gateway.controllers;

import br.com.zup.gateway.controllers.dtos.ConsumerAddressRegisterDTO;
import br.com.zup.gateway.controllers.dtos.ConsumerAddressResponseDTO;
import br.com.zup.gateway.controllers.dtos.ConsumerAddressUpdateDTO;
import br.com.zup.gateway.infra.clients.address.dtos.AddressRegisterDTO;
import br.com.zup.gateway.infra.clients.address.dtos.AddressResponseDTO;
import br.com.zup.gateway.infra.clients.consumer.dtos.ConsumerRegisterDTO;
import br.com.zup.gateway.infra.clients.consumer.dtos.ConsumerResponseDTO;
import br.com.zup.gateway.services.ConsumerAddressService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consumer-address")
public class ConsumerAddressController {

    private static final Logger log = LoggerFactory.getLogger(ConsumerAddressController.class);

    @Autowired
    private ConsumerAddressService consumerAddressService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody ConsumerAddressRegisterDTO registerDTO){
        try {
            log.info("Start register consumer and address flow");
            ConsumerAddressResponseDTO response = consumerAddressService.registerConsumerAddress(registerDTO);
            log.info("Finish register consumer and address flow");
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.status(400).body("Validation error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error to register consumer address: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ConsumerAddressResponseDTO>> getConsumerAndAddressById(
            @RequestParam String consumerId,
            @RequestParam String addressId) {
        try {
            log.info("Start get consumer and address by id (consumerId: {}, addressId: {})", consumerId, addressId);
            List<ConsumerAddressResponseDTO> response = consumerAddressService.getConsumerAndAddressById(consumerId, addressId);
            log.info("Finish get consumer and address by id");
            return ResponseEntity.status(200).body(response);
        } catch (RuntimeException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteConsumerAndAddress(
            @RequestParam String consumerId,
            @RequestParam String addressId) {
        try {
            log.info("Start deleting consumer and address (consumerId: {}, addressId: {})", consumerId, addressId);

            consumerAddressService.deleteConsumerById(consumerId);
            consumerAddressService.deleteAddressById(addressId);

            log.info("Finish deleting consumer and address");
            return ResponseEntity.status(204).build();
        } catch (RuntimeException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping
    public ResponseEntity<ConsumerAddressResponseDTO> updateConsumerAndAddress(
            @RequestParam String consumerId,
            @RequestParam String addressId,
            @RequestBody ConsumerAddressUpdateDTO updateDTO) {
        try {
            log.info("Start updating consumer and address (consumerId: {}, addressId: {})", consumerId, addressId);
            ConsumerResponseDTO updatedConsumer = consumerAddressService.updateConsumer(consumerId, updateDTO.getConsumer());
            AddressResponseDTO updatedAddress = consumerAddressService.updateAddress(addressId, updateDTO.getAddress());

            ConsumerAddressResponseDTO response = new ConsumerAddressResponseDTO(updatedConsumer, updatedAddress);

            log.info("Finish updating consumer and address");
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
