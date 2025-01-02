package br.com.zup.consumer.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
public class Consumer {

    @Id
    @UuidGenerator
    private String id;
    private String name;
    private String age;
    private String email;
}
