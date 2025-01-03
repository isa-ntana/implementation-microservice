package br.com.zup.gateway.controllers;

import br.com.zup.gateway.infra.clients.address.dtos.AddressRegisterDTO;
import br.com.zup.gateway.infra.clients.address.dtos.AddressResponseDTO;
import br.com.zup.gateway.services.ConsumerAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {
    private static final Logger log = LoggerFactory.getLogger(ConsumerAddressController.class);

    @Autowired
    private ConsumerAddressService consumerAddressService;

    @GetMapping("/all")
    public ResponseEntity<List<AddressResponseDTO>> getAllAddresses(){
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

    @DeleteMapping("/{addressId}")
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

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @PathVariable String addressId,
            @RequestBody AddressRegisterDTO registerDTO) {
        try {
            log.info("Start updating address with ID: {}", addressId);
            AddressResponseDTO response = consumerAddressService.updateAddress(addressId, registerDTO);
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
