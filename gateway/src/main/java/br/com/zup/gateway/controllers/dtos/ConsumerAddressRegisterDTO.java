package br.com.zup.gateway.controllers.dtos;

import lombok.Data;

@Data
public class ConsumerAddressRegisterDTO {
    private String name;
    private String age;
    private String email;

    private AddressDTO address;

    public ConsumerAddressRegisterDTO() {
    }
}
