package br.com.zup.gateway.controllers;

import br.com.zup.gateway.controllers.dtos.ConsumerAddressRegisterDTO;
import br.com.zup.gateway.controllers.dtos.ConsumerAddressResponseDTO;
import br.com.zup.gateway.infra.clients.address.dtos.AddressResponseDTO;
import br.com.zup.gateway.infra.clients.consumer.dtos.ConsumerResponseDTO;
import br.com.zup.gateway.services.ConsumerAddressService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/address")
    public ResponseEntity<List<AddressResponseDTO>> findAllAddresses(){
        try {
            log.info("Start retrieving all addresses");
            List<AddressResponseDTO> response = consumerAddressService.getAllAddresses();
            log.info("Finish retrieving all addresses");
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            log.error("Error occurred while fetching addresses: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/consumer")
    public ResponseEntity<List<ConsumerResponseDTO>> findAllConsumerAddresses(){
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


    @DeleteMapping("/consumer/{consumerId}")
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

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String addressId) {
        try {
            log.info("Start deleting address with ID: {}", addressId);
            consumerAddressService.deleteAddressById(addressId);
            log.info("Finish deleting address with ID: {}", addressId);
            return ResponseEntity.status(204).build();
        } catch (RuntimeException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/consumer/{consumerId}")
    public ResponseEntity<ConsumerAddressResponseDTO> updateConsumer(
            @PathVariable String consumerId,
            @RequestBody ConsumerAddressRegisterDTO registerDTO) {
        try {
            log.info("Start updating consumer with ID: {}", consumerId);
            ConsumerAddressResponseDTO response = consumerAddressService.updateConsumer(consumerId, registerDTO);
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

    @PutMapping("/address/{addressId}")
    public ResponseEntity<ConsumerAddressResponseDTO> updateAddress(
            @PathVariable String addressId,
            @RequestBody ConsumerAddressRegisterDTO registerDTO) {
        try {
            log.info("Start updating address with ID: {}", addressId);
            ConsumerAddressResponseDTO response = consumerAddressService.updateAddress(addressId, registerDTO);
            log.info("Finish updating address with ID: {}", addressId);
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
