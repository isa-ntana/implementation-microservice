package br.com.zup.address.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
public class Address {

    @Id
    @UuidGenerator
    private String id;
    private String street;
    private String city;
    private String zipCode;
    private String state;
    @Column(nullable = false)
    private String consumerId;

    public Address() {
    }
}
