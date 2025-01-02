package br.com.zup.gateway.services;

import br.com.zup.gateway.controllers.ConsumerAddressController;
import br.com.zup.gateway.controllers.dtos.ConsumerAddressRegisterDTO;
import br.com.zup.gateway.controllers.dtos.ConsumerAddressResponseDTO;
import br.com.zup.gateway.infra.clients.address.AddressClient;
import br.com.zup.gateway.infra.clients.address.dtos.AddressRegisterDTO;
import br.com.zup.gateway.infra.clients.address.dtos.AddressResponseDTO;
import br.com.zup.gateway.infra.clients.consumer.ConsumerClient;
import br.com.zup.gateway.infra.clients.consumer.dtos.ConsumerRegisterDTO;
import br.com.zup.gateway.infra.clients.consumer.dtos.ConsumerResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumerAddressService {

    private static final Logger log = LoggerFactory.getLogger(ConsumerAddressController.class);

    @Autowired
    private ConsumerClient consumerClient;

    @Autowired
    private AddressClient addressClient;

    public List<AddressResponseDTO> getAllAddresses() {
        return addressClient.getAllAddresses();
    }

    public List<ConsumerResponseDTO> getAllConsumers() {
        return consumerClient.getAllConsumers();
    }

    public ConsumerResponseDTO getConsumerById(String consumerId) {
        return consumerClient.getConsumerById(consumerId);
    }

    public AddressResponseDTO getAddressById(String addressId) {
        return addressClient.getAddressById(addressId);
    }

    public List<ConsumerAddressResponseDTO> getConsumerAndAddressById(String consumerId, String addressId) {
        log.info("Start get Consumer And Address By Id flow");
        ConsumerResponseDTO consumerResponseDTO = getConsumerById(consumerId);
        AddressResponseDTO addressResponseDTO = getAddressById(addressId);
        ConsumerAddressResponseDTO consumerAddressResponseDTO =
                new ConsumerAddressResponseDTO(consumerResponseDTO, addressResponseDTO);
        log.info("Finish get Consumer And Address By Id flow");
        return List.of(consumerAddressResponseDTO);
    }

    public ConsumerAddressResponseDTO registerConsumerAddress(ConsumerAddressRegisterDTO consumerAddressRegisterDTO) {
        log.info("Start register Consumer with Address");
        ConsumerResponseDTO consumerResponseDTO = registerConsumer(consumerAddressRegisterDTO);
        AddressResponseDTO addressResponseDTO = registerAddress(consumerAddressRegisterDTO, consumerResponseDTO.getId());
        log.info("Finish register Consumer with Address");
        return new ConsumerAddressResponseDTO(consumerResponseDTO, addressResponseDTO);
    }

    private ConsumerResponseDTO registerConsumer(ConsumerAddressRegisterDTO consumerAddressRegisterDTO) {
        log.info("Start register Consumer");
        ConsumerRegisterDTO consumerRegisterDTO = mapToConsumerRegisterDTO(consumerAddressRegisterDTO);
        log.info("Finish register Consumer");
        return consumerClient.registerConsumerClient(consumerRegisterDTO);
    }

    private AddressResponseDTO registerAddress(ConsumerAddressRegisterDTO consumerAddressRegisterDTO, String consumerId) {
        log.info("Start register Address");
        AddressRegisterDTO addressRegisterDto = mapToAddressRegisterDTO(consumerAddressRegisterDTO, consumerId);
        log.info("Finish register Address");
        return addressClient.registeAddress(addressRegisterDto);
    }

    private ConsumerRegisterDTO mapToConsumerRegisterDTO(ConsumerAddressRegisterDTO consumerAddressRegisterDTO) {
        ConsumerRegisterDTO consumerRegisterDTO = new ConsumerRegisterDTO();
        consumerRegisterDTO.setAge(consumerAddressRegisterDTO.getAge());
        consumerRegisterDTO.setEmail(consumerAddressRegisterDTO.getEmail());
        consumerRegisterDTO.setName(consumerAddressRegisterDTO.getName());
        return consumerRegisterDTO;
    }

    private AddressRegisterDTO mapToAddressRegisterDTO(ConsumerAddressRegisterDTO consumerAddressRegisterDTO, String consumerId) {
        AddressRegisterDTO addressRegisterDto = new AddressRegisterDTO();
        addressRegisterDto.setConsumerId(consumerId);
        addressRegisterDto.setCity(consumerAddressRegisterDTO.getAddress().getCity());
        addressRegisterDto.setState(consumerAddressRegisterDTO.getAddress().getState());
        addressRegisterDto.setStreet(consumerAddressRegisterDTO.getAddress().getStreet());
        addressRegisterDto.setZipCode(consumerAddressRegisterDTO.getAddress().getZipCode());
        return addressRegisterDto;
    }

    public void deleteConsumerById(String consumerId) {
        consumerClient.deleteConsumerById(consumerId);
    }

    public void deleteAddressById(String addressId) {
        addressClient.deleteAddressById(addressId);
    }

    public ConsumerAddressResponseDTO updateConsumer(String consumerId, ConsumerAddressRegisterDTO registerDTO) {
        log.info("Start update Consumer with Id flow");
        ConsumerRegisterDTO consumerRegisterDTO = mapToConsumerRegisterDTO(registerDTO);
        ConsumerResponseDTO updatedConsumer = consumerClient.updateConsumer(consumerId, consumerRegisterDTO);
        AddressResponseDTO addressResponseDTO = addressClient.getAddressById(consumerId);
        log.info("Finish update Consumer with Id flow");
        return new ConsumerAddressResponseDTO(updatedConsumer, addressResponseDTO);
    }

    public ConsumerAddressResponseDTO updateAddress(String addressId, ConsumerAddressRegisterDTO registerDTO) {
        log.info("Start update Address with Id flow");
        AddressRegisterDTO addressRegisterDto = mapToAddressRegisterDTO(registerDTO, addressId);
        AddressResponseDTO updatedAddress = addressClient.updateAddress(addressId, addressRegisterDto);
        ConsumerResponseDTO consumerResponseDTO = consumerClient.getConsumerById(registerDTO.getAddress().getConsumerId());
        log.info("Finish update Address with Id flow");
        return new ConsumerAddressResponseDTO(consumerResponseDTO, updatedAddress);
    }
}
