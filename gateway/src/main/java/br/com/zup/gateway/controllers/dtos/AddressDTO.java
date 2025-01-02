package br.com.zup.gateway.controllers.dtos;

import br.com.zup.gateway.infra.clients.address.dtos.AddressResponseDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
public class AddressDTO {
    private String street;
    private String city;
    private String zipCode;
    private String state;
    private String consumerId;

    public AddressDTO() {
    }

    public AddressDTO(AddressResponseDTO addressResponseDTO) {
        this.city = addressResponseDTO.getCity();
        this.consumerId = addressResponseDTO.getConsumerId();
        this.zipCode = addressResponseDTO.getZipCode();
        this.state = addressResponseDTO.getState();
        this.street = addressResponseDTO.getStreet();
    }
}
