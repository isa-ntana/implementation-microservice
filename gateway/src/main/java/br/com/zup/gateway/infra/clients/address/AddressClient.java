package br.com.zup.gateway.infra.clients.address;

import br.com.zup.gateway.infra.clients.address.dtos.AddressRegisterDTO;
import br.com.zup.gateway.infra.clients.address.dtos.AddressResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class AddressClient {

    @Autowired
    private WebClient webClient;
    private final String URL_BASE = "http://localhost:8082/address";

    public AddressResponseDTO registeAddress(AddressRegisterDTO addressRegisterDto){
        return webClient.post()
                .uri(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(addressRegisterDto)
                .retrieve()
                .bodyToMono(AddressResponseDTO.class)
                .block();
    }

    public List<AddressResponseDTO> getAllAddresses(){
        return webClient
                .get()
                .uri(URL_BASE)
                .retrieve()
                .bodyToFlux(AddressResponseDTO.class)
                .collectList()
                .block();
    }

    public AddressResponseDTO getAddressById(String addressId){
        return webClient
                .get()
                .uri(URL_BASE+"/"+addressId)
                .retrieve()
                .bodyToMono(AddressResponseDTO.class)
                .block();
    }

    public void deleteAddressById(String addressId) {
        webClient
                .delete()
                .uri(URL_BASE + "/" + addressId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public AddressResponseDTO updateAddress(String addressId, AddressRegisterDTO addressRegisterDto) {
        return webClient
                .put()
                .uri(URL_BASE + "/" + addressId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(addressRegisterDto)
                .retrieve()
                .bodyToMono(AddressResponseDTO.class)
                .block();
    }

}
