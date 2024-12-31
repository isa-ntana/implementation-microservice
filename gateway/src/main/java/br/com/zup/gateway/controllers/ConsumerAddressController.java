package br.com.zup.gateway.controllers;

import br.com.zup.gateway.controllers.dtos.ConsumerAddressRegisterDTO;
import br.com.zup.gateway.controllers.dtos.ConsumerAddressResponseDTO;
import br.com.zup.gateway.infra.clients.address.dtos.AddressResponseDTO;
import br.com.zup.gateway.infra.clients.consumer.dtos.ConsumerResponseDTO;
import br.com.zup.gateway.services.ConsumerAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consumer-address")
public class ConsumerAddressController {

    @Autowired
    private ConsumerAddressService consumerAddressService;

    @PostMapping
    public ConsumerAddressResponseDTO register(@RequestBody ConsumerAddressRegisterDTO registerDTO){
        return consumerAddressService.registerConsumerAddress(registerDTO);
    }

    @GetMapping
    public List<ConsumerAddressResponseDTO> getConsumerAndAddressById(
            @RequestParam String consumerId,
            @RequestParam String addressId) {
        return consumerAddressService.getConsumerAndAddressById(consumerId, addressId);
    }

    @GetMapping("/address")
    public List<AddressResponseDTO> findAllAddresses(){
        return consumerAddressService.getAllAddresses();
    }

    @GetMapping("/consumer")
    public List<ConsumerResponseDTO> findAllConsumerAddresses(){
        return consumerAddressService.getAllConsumers();
    }


    @DeleteMapping("/consumer/{consumerId}")
    public void deleteConsumer(@PathVariable String consumerId) {
        consumerAddressService.deleteConsumerById(consumerId);
    }

    @DeleteMapping("/address/{addressId}")
    public void deleteAddress(@PathVariable String addressId) {
        consumerAddressService.deleteAddressById(addressId);
    }

    @PutMapping("/consumer/{consumerId}")
    public ConsumerAddressResponseDTO updateConsumer(
            @PathVariable String consumerId,
            @RequestBody ConsumerAddressRegisterDTO registerDTO) {
        return consumerAddressService.updateConsumer(consumerId, registerDTO);
    }

    @PutMapping("/address/{addressId}")
    public ConsumerAddressResponseDTO updateAddress(
            @PathVariable String addressId,
            @RequestBody ConsumerAddressRegisterDTO registerDTO) {
        return consumerAddressService.updateAddress(addressId, registerDTO);
    }
}
