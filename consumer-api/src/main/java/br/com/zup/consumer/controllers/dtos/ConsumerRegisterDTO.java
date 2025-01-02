package br.com.zup.consumer.controllers.dtos;

import br.com.zup.consumer.models.Consumer;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ConsumerRegisterDTO {

    @NotNull
    private String name;

    @NotNull
    @Size(min = 1, max = 3)
    private String age;

    @Email
    private String email;

    public ConsumerRegisterDTO() {
    }

    public Consumer toEntity() {
        Consumer consumer = new Consumer();
        consumer.setName(this.name);
        consumer.setAge(this.age);
        consumer.setEmail(this.email);
        return consumer;
    }
}
