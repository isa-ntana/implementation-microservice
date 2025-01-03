package br.com.zup.gateway;

import br.com.zup.gateway.controllers.ConsumerAddressController;
import br.com.zup.gateway.controllers.dtos.ConsumerAddressRegisterDTO;
import br.com.zup.gateway.controllers.dtos.ConsumerAddressResponseDTO;
import br.com.zup.gateway.infra.clients.address.dtos.AddressRegisterDTO;
import br.com.zup.gateway.infra.clients.address.dtos.AddressResponseDTO;
import br.com.zup.gateway.infra.clients.consumer.dtos.ConsumerRegisterDTO;
import br.com.zup.gateway.infra.clients.consumer.dtos.ConsumerResponseDTO;
import br.com.zup.gateway.services.ConsumerAddressService;
import br.com.zup.gateway.infra.clients.consumer.ConsumerClient;
import br.com.zup.gateway.infra.clients.address.AddressClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class GatewayApplicationTests {

    private MockMvc mockMvc;

    @Mock
    private ConsumerAddressService consumerAddressService;

    @Mock
    private ConsumerClient consumerClient;

    @Mock
    private AddressClient addressClient;

    @InjectMocks
    private ConsumerAddressController consumerAddressController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(consumerAddressController).build();
    }

    @Test
    void testRegisterConsumerAddressSuccess() throws Exception {
        ConsumerAddressRegisterDTO registerDTO = new ConsumerAddressRegisterDTO();
        registerDTO.setName("John Doe");
        registerDTO.setAge("30");
        registerDTO.setEmail("john.doe@example.com");

        ConsumerAddressResponseDTO responseDTO = new ConsumerAddressResponseDTO();
        responseDTO.setId("1");
        responseDTO.setName("John Doe");
        responseDTO.setAge("30");
        responseDTO.setEmail("john.doe@example.com");

        when(consumerAddressService.registerConsumerAddress(any(ConsumerAddressRegisterDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/consumer-address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\"," +
                                "\"age\":\"30\"," +
                                "\"email\":\"john.doe@example.com\"," +
                                "\"address\":" +
                                "{\"street\":\"Main St\"," +
                                "\"city\":\"Springfield\"," +
                                "\"state\":\"SP\"," +
                                "\"zipCode\":\"12345\"}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value("30"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testRegisterConsumerAddressValidationError() throws Exception {
        ConsumerAddressRegisterDTO registerDTO = new ConsumerAddressRegisterDTO();
        registerDTO.setName("John Doe");
        registerDTO.setAge("30");
        registerDTO.setEmail("invalid-email");

        when(consumerAddressService.registerConsumerAddress(any(ConsumerAddressRegisterDTO.class)))
                .thenThrow(new IllegalArgumentException("Validation error"));

        mockMvc.perform(post("/consumer-address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"age\":\"30\",\"email\":\"invalid-email\",\"address\":{\"street\":\"Main St\",\"city\":\"Springfield\",\"state\":\"SP\",\"zipCode\":\"12345\"}}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Validation error: Validation error"));
    }

    @Test
    void testGetConsumerAndAddressByIdSuccess() throws Exception {
        ConsumerAddressResponseDTO responseDTO = new ConsumerAddressResponseDTO();
        responseDTO.setId("1");
        responseDTO.setName("John Doe");
        responseDTO.setAge("30");
        responseDTO.setEmail("john.doe@example.com");

        when(consumerAddressService.getConsumerAndAddressById("1", "1")).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/consumer-address?consumerId=1&addressId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].age").value("30"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
    }

    @Test
    void testDeleteConsumerAndAddressSuccess() throws Exception {
        doNothing().when(consumerAddressService).deleteConsumerById("1");
        doNothing().when(consumerAddressService).deleteAddressById("1");

        mockMvc.perform(delete("/consumer-address?consumerId=1&addressId=1"))
                .andExpect(status().isNoContent());

        verify(consumerAddressService, times(1)).deleteConsumerById("1");
        verify(consumerAddressService, times(1)).deleteAddressById("1");
    }

    @Test
    void testUpdateConsumerAndAddressSuccess() throws Exception {
        ConsumerResponseDTO consumerResponse = new ConsumerResponseDTO();
        consumerResponse.setName("Updated Name");
        consumerResponse.setAge("35");
        consumerResponse.setEmail("updated@example.com");

        AddressResponseDTO addressResponse = new AddressResponseDTO();
        addressResponse.setStreet("Updated Street");
        addressResponse.setCity("Updated City");
        addressResponse.setState("Updated State");
        addressResponse.setZipCode("54321");

        when(consumerAddressService.updateConsumer(eq("1"), any(ConsumerRegisterDTO.class)))
                .thenReturn(consumerResponse);
        when(consumerAddressService.updateAddress(eq("1"), any(AddressRegisterDTO.class)))
                .thenReturn(addressResponse);

        String updateRequestBody = "{\n" +
                "  \"consumer\": {\n" +
                "    \"name\": \"Updated Name\",\n" +
                "    \"age\": \"35\",\n" +
                "    \"email\": \"updated@example.com\"\n" +
                "  },\n" +
                "  \"address\": {\n" +
                "    \"street\": \"Updated Street\",\n" +
                "    \"city\": \"Updated City\",\n" +
                "    \"state\": \"Updated State\",\n" +
                "    \"zipCode\": \"54321\"\n" +
                "  }\n" +
                "}";

        mockMvc.perform(put("/consumer-address?consumerId=1&addressId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.age").value("35"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.address.street").value("Updated Street"))
                .andExpect(jsonPath("$.address.city").value("Updated City"))
                .andExpect(jsonPath("$.address.state").value("Updated State"))
                .andExpect(jsonPath("$.address.zipCode").value("54321"));
    }

    @Test
    void testUpdateConsumerAndAddressFailureConsumerNotFound() throws Exception {
        when(consumerAddressService.updateConsumer(eq("1"), any(ConsumerRegisterDTO.class)))
                .thenThrow(new RuntimeException("Consumer not found"));

        String updateRequestBody = "{\n" +
                "  \"consumer\": {\n" +
                "    \"name\": \"Updated Name\",\n" +
                "    \"age\": \"35\",\n" +
                "    \"email\": \"updated@example.com\"\n" +
                "  },\n" +
                "  \"address\": {\n" +
                "    \"street\": \"Updated Street\",\n" +
                "    \"city\": \"Updated City\",\n" +
                "    \"state\": \"Updated State\",\n" +
                "    \"zipCode\": \"54321\"\n" +
                "  }\n" +
                "}";

        mockMvc.perform(put("/consumer-address?consumerId=1&addressId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateConsumerAndAddressFailureAddressNotFound() throws Exception {
        when(consumerAddressService.updateAddress(eq("1"), any(AddressRegisterDTO.class)))
                .thenThrow(new RuntimeException("Address not found"));

        String updateRequestBody = "{\n" +
                "  \"consumer\": {\n" +
                "    \"name\": \"Updated Name\",\n" +
                "    \"age\": \"35\",\n" +
                "    \"email\": \"updated@example.com\"\n" +
                "  },\n" +
                "  \"address\": {\n" +
                "    \"street\": \"Updated Street\",\n" +
                "    \"city\": \"Updated City\",\n" +
                "    \"state\": \"Updated State\",\n" +
                "    \"zipCode\": \"54321\"\n" +
                "}";

        mockMvc.perform(put("/consumer-address?consumerId=1&addressId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestBody))
                .andExpect(status().is4xxClientError());
    }

}
