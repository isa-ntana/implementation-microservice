package br.com.zup.gateway.services;

import br.com.zup.gateway.controllers.dtos.ConsumerAddressRegisterDTO;
import br.com.zup.gateway.controllers.dtos.ConsumerAddressResponseDTO;
import br.com.zup.gateway.infra.clients.address.AddressClient;
import br.com.zup.gateway.infra.clients.address.dtos.AddressRegisterDto;
import br.com.zup.gateway.infra.clients.address.dtos.AddressResponseDTO;
import br.com.zup.gateway.infra.clients.consumer.ConsumerClient;
import br.com.zup.gateway.infra.clients.consumer.dtos.ConsumerRegisterDTO;
import br.com.zup.gateway.infra.clients.consumer.dtos.ConsumerResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumerAddressService {

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
        ConsumerResponseDTO consumerResponseDTO = getConsumerById(consumerId);
        AddressResponseDTO addressResponseDTO = getAddressById(addressId);
        ConsumerAddressResponseDTO consumerAddressResponseDTO =
                new ConsumerAddressResponseDTO(consumerResponseDTO, addressResponseDTO);

        return List.of(consumerAddressResponseDTO);
    }

    public ConsumerAddressResponseDTO registerConsumerAddress(ConsumerAddressRegisterDTO consumerAddressRegisterDTO) {
        ConsumerResponseDTO consumerResponseDTO = registerConsumer(consumerAddressRegisterDTO);
        AddressResponseDTO addressResponseDTO = registerAddress(consumerAddressRegisterDTO, consumerResponseDTO.getId());
        return new ConsumerAddressResponseDTO(consumerResponseDTO, addressResponseDTO);
    }

    private ConsumerResponseDTO registerConsumer(ConsumerAddressRegisterDTO consumerAddressRegisterDTO) {
        ConsumerRegisterDTO consumerRegisterDTO = mapToConsumerRegisterDTO(consumerAddressRegisterDTO);
        return consumerClient.registerConsumerClient(consumerRegisterDTO);
    }

    private AddressResponseDTO registerAddress(ConsumerAddressRegisterDTO consumerAddressRegisterDTO, String consumerId) {
        AddressRegisterDto addressRegisterDto = mapToAddressRegisterDTO(consumerAddressRegisterDTO, consumerId);
        return addressClient.registeAddress(addressRegisterDto);
    }

    private ConsumerRegisterDTO mapToConsumerRegisterDTO(ConsumerAddressRegisterDTO consumerAddressRegisterDTO) {
        ConsumerRegisterDTO consumerRegisterDTO = new ConsumerRegisterDTO();
        consumerRegisterDTO.setAge(consumerAddressRegisterDTO.getAge());
        consumerRegisterDTO.setEmail(consumerAddressRegisterDTO.getEmail());
        consumerRegisterDTO.setName(consumerAddressRegisterDTO.getName());
        return consumerRegisterDTO;
    }

    private AddressRegisterDto mapToAddressRegisterDTO(ConsumerAddressRegisterDTO consumerAddressRegisterDTO, String consumerId) {
        AddressRegisterDto addressRegisterDto = new AddressRegisterDto();
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
        ConsumerRegisterDTO consumerRegisterDTO = mapToConsumerRegisterDTO(registerDTO);
        ConsumerResponseDTO updatedConsumer = consumerClient.updateConsumer(consumerId, consumerRegisterDTO);
        AddressResponseDTO addressResponseDTO = addressClient.getAddressById(consumerId);

        return new ConsumerAddressResponseDTO(updatedConsumer, addressResponseDTO);
    }

    public ConsumerAddressResponseDTO updateAddress(String addressId, ConsumerAddressRegisterDTO registerDTO) {
        AddressRegisterDto addressRegisterDto = mapToAddressRegisterDTO(registerDTO, addressId);
        AddressResponseDTO updatedAddress = addressClient.updateAddress(addressId, addressRegisterDto);

        ConsumerResponseDTO consumerResponseDTO = consumerClient.getConsumerById(registerDTO.getAddress().getConsumerId());

        return new ConsumerAddressResponseDTO(consumerResponseDTO, updatedAddress);
    }
}
