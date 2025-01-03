package br.com.zup.gateway.controllers.dtos;

import br.com.zup.gateway.infra.clients.address.dtos.AddressRegisterDTO;
import br.com.zup.gateway.infra.clients.consumer.dtos.ConsumerRegisterDTO;
import lombok.Data;

@Data
public class ConsumerAddressUpdateDTO {

    private ConsumerRegisterDTO consumer;
    private AddressRegisterDTO address;

    public ConsumerAddressUpdateDTO() {
    }
}
