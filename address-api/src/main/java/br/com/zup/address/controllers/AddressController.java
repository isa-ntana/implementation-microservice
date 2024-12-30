package br.com.zup.address.controllers;

import br.com.zup.address.controllers.dtos.AddressRequestDTO;
import br.com.zup.address.controllers.dtos.AddressResponseDTO;
import br.com.zup.address.models.Address;
import br.com.zup.address.services.AddressService;
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
    public ResponseEntity<AddressResponseDTO> createAddress(@RequestBody AddressRequestDTO requestDTO) {
        log.info("Start address register flow");
        Address address = new Address();
        address.setStreet(requestDTO.getStreet());
        address.setCity(requestDTO.getCity());
        address.setZipCode(requestDTO.getZipCode());
        address.setState(requestDTO.getState());
        address.setConsumerId(requestDTO.getConsumerId());

        Address createdAddress = addressService.createAddress(address);

        AddressResponseDTO responseDTO = new AddressResponseDTO();
        responseDTO.setId(createdAddress.getId());
        responseDTO.setStreet(createdAddress.getStreet());
        responseDTO.setCity(createdAddress.getCity());
        responseDTO.setZipCode(createdAddress.getZipCode());
        responseDTO.setState(createdAddress.getState());
        responseDTO.setConsumerId(createdAddress.getConsumerId());
        log.info("Finish address register flow");
        return ResponseEntity.ok(responseDTO);
    }

    // Read (Get All)
    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAllAddresses() {
        log.info("Start get all addresses flow");
        List<Address> addresses = addressService.getAllAddresses();
        List<AddressResponseDTO> responseDTOs = addresses.stream().map(address -> {
            AddressResponseDTO dto = new AddressResponseDTO();
            dto.setId(address.getId());
            dto.setStreet(address.getStreet());
            dto.setCity(address.getCity());
            dto.setZipCode(address.getZipCode());
            dto.setState(address.getState());
            dto.setConsumerId(address.getConsumerId());
            return dto;
        }).collect(Collectors.toList());

        log.info("Finish get all addresses flow");
        return ResponseEntity.ok(responseDTOs);
    }

    // Read (Get by ID)
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> getAddressById(@PathVariable String id) {
        log.info("Start get address by id flow");
        Address address = addressService.getAddressById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id " + id));

        AddressResponseDTO responseDTO = new AddressResponseDTO();
        responseDTO.setId(address.getId());
        responseDTO.setStreet(address.getStreet());
        responseDTO.setCity(address.getCity());
        responseDTO.setZipCode(address.getZipCode());
        responseDTO.setState(address.getState());
        responseDTO.setConsumerId(address.getConsumerId());

        log.info("Finish get address by id flow");
        return ResponseEntity.ok(responseDTO);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> updateAddress(@PathVariable String id, @RequestBody AddressRequestDTO requestDTO) {
        log.info("Start update address by id flow");
        Address updatedAddress = new Address();
        updatedAddress.setStreet(requestDTO.getStreet());
        updatedAddress.setCity(requestDTO.getCity());
        updatedAddress.setZipCode(requestDTO.getZipCode());
        updatedAddress.setState(requestDTO.getState());
        updatedAddress.setConsumerId(requestDTO.getConsumerId());

        Address address = addressService.updateAddress(id, updatedAddress);

        AddressResponseDTO responseDTO = new AddressResponseDTO();
        responseDTO.setId(address.getId());
        responseDTO.setStreet(address.getStreet());
        responseDTO.setCity(address.getCity());
        responseDTO.setZipCode(address.getZipCode());
        responseDTO.setState(address.getState());
        responseDTO.setConsumerId(address.getConsumerId());

        log.info("Finish update address by id flow");
        return ResponseEntity.ok(responseDTO);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String id) {
        log.info("Start delete address by id flow");
        addressService.deleteAddress(id);
        log.info("Finish delete address by id flow");
        return ResponseEntity.noContent().build();
    }
}