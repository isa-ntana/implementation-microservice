package br.com.zup.address.controllers.dtos;

import br.com.zup.address.models.Address;
import lombok.*;

@Data
@AllArgsConstructor
public class AddressResponseDTO {

    private String id;
    private String street;
    private String city;
    private String zipCode;
    private String state;
    private String consumerId;

    public AddressResponseDTO() {
    }

    public static AddressResponseDTO fromEntity(Address address) {
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setZipCode(address.getZipCode());
        dto.setState(address.getState());
        dto.setConsumerId(address.getConsumerId());
        return dto;
    }
}
