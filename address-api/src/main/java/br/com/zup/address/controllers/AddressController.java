package br.com.zup.address.controllers;

import br.com.zup.address.controllers.dtos.AddressRequestDTO;
import br.com.zup.address.controllers.dtos.AddressResponseDTO;
import br.com.zup.address.models.Address;
import br.com.zup.address.services.AddressService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/address")
public class AddressController {
    private static final Logger log = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressService addressService;

    // Create
    @PostMapping
    public ResponseEntity<AddressResponseDTO> createAddress(@Valid @RequestBody AddressRequestDTO requestDTO) {
        try {
            log.info("Start address register flow");
            Address address = addressService.createAddress(requestDTO.toEntity());
            log.info("Finish consumer register flow");
            return ResponseEntity.status(201).body(AddressResponseDTO.fromEntity(address));
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return createErrorResponseDTO(e.getMessage(), 400);
        } catch (RuntimeException e) {
            log.error("Error occurred while creating address: {}", e.getMessage());
            return createErrorResponseDTO("An error occurred while creating the address: " + e.getMessage(), 500);
        }
    }

    // Read (Get All)
    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAllAddresses() {
        log.info("Start get all addresses flow");

        List<Address> addresses = addressService.getAllAddresses();
        List<AddressResponseDTO> responseDTOs = addresses.stream()
                .map(AddressResponseDTO::fromEntity)
                .collect(Collectors.toList());

        log.info("Finish get all addresses flow");
        return ResponseEntity.status(200).body(responseDTOs);
    }

    // Read (Get by ID)
    @GetMapping("/{id}")
    public ResponseEntity<Object> getAddressById(@Valid @PathVariable String id) {
        try {
            log.info("Start get address by id flow");

            Address address = addressService.getAddressById(id)
                    .orElseThrow(() -> new RuntimeException("Address not found with id " + id));

            log.info("Finish get address by id flow");
            return ResponseEntity.status(200).body(AddressResponseDTO.fromEntity(address));
        } catch (RuntimeException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(404).body("Address not found with id: " + id);
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(505).body("An unexpected error occurred: " + e.getMessage());
        }
    }


    // Update
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> updateAddress(@Valid @PathVariable String id, @RequestBody AddressRequestDTO requestDTO) {
        try {
            log.info("Start update address by id flow");
            Address updatedAddress = addressService.updateAddress(id, requestDTO.toEntity());
            log.info("Finish update address by id flow");
            return ResponseEntity.status(200).body(AddressResponseDTO.fromEntity(updatedAddress));
        } catch (RuntimeException e) {
            log.error("Error occurred while creating address: {}", e.getMessage());
            return createErrorResponseDTO("An error occurred while updating the address: " + e.getMessage(), 500);
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String id) {
        try {
            log.info("Start delete address by id flow");
            addressService.deleteAddress(id);
            log.info("Finish delete address by id flow");
            return ResponseEntity.status(204).build();
        } catch (RuntimeException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    private ResponseEntity<AddressResponseDTO> createErrorResponseDTO(String message, int statusCode) {
        AddressResponseDTO errorResponse = new AddressResponseDTO();
        errorResponse.setStreet("Error: " + message);
        return ResponseEntity.status(statusCode).body(errorResponse);
    }
}