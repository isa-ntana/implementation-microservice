package br.com.zup.gateway.infra.clients.address.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressRegisterDTO {
    @NotNull
    private String street;
    @NotNull
    private String city;
    @NotNull
    private String zipCode;
    @NotNull
    private String state;
    private String consumerId;

    public AddressRegisterDTO() {
    }
}
