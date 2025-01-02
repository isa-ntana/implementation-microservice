package br.com.zup.address.controllers.dtos;

import br.com.zup.address.models.Address;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class AddressRequestDTO {

    @NotNull
    private String street;

    @NotNull
    private String city;

    @Pattern(regexp = "^[0-9]{5}-[0-9]{3}$", message = "Invalid zip code")
    @NotNull
    private String zipCode;

    @NotNull
    private String state;

    private String consumerId;

    public AddressRequestDTO() {
    }

    public Address toEntity() {
        Address address = new Address();
        address.setCity(this.city);
        address.setStreet(this.street);
        address.setZipCode(this.zipCode);
        address.setState(this.state);
        address.setConsumerId(this.consumerId);
        return address;
    }

}
