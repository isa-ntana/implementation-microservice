package br.com.zup.gateway.infra.clients.consumer.dtos;

import lombok.Data;

@Data
public class ConsumerRegisterDTO {
    private String name;
    private String age;
    private String email;

    public ConsumerRegisterDTO() {
    }

}

