package galerium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import galerium.config.TestSecurityConfig;
import galerium.dto.client.ClientRequestDTO;
import galerium.dto.client.ClientResponseDTO;
import galerium.enums.UserRole;
import galerium.service.interfaces.ClientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = ClientController.class)
class ClientControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockBean
    ClientService clientService;

    // ------- TESTING POST /api/clients -------
    @Test
    @DisplayName("POST /api/clients -> 201 CREATED")
    void createClient_created() throws Exception {
        var req = new ClientRequestDTO();
        req.setFullName("John Doe");
        req.setEmail("johndoe@mail.com");
        req.setPassword("Password123!");
        req.setPhoneNumber("+1234567890");
        req.setAddress("123 Main St, Anytown, USA");

        var created = resp(1L, "John Doe", "johndoe@mail.com", null, "+1234567890","123 Main St, Anytown, USA", true, UserRole.CLIENT, LocalDateTime.now());
        when(clientService.createClient(any())).thenReturn(created);

        var result = mvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/clients/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andReturn();

        ArgumentCaptor<ClientRequestDTO> captor = ArgumentCaptor.forClass(ClientRequestDTO.class);
        verify(clientService).createClient(captor.capture());
        var sent = captor.getValue();
        assertThat(sent.getFullName()).isEqualTo("John Doe");
        assertThat(sent.getEmail()).isEqualTo("johndoe@mail.com");
        assertThat(sent.getPhoneNumber()).isEqualTo("+1234567890");
        assertThat(sent.getAddress()).isEqualTo("123 Main St, Anytown, USA");
    }

    @Test
    void updateClient() {
    }

    @Test
    void getAllClients() {
    }

    @Test
    void getClientById() {
    }

    @Test
    void deleteClient() {
    }

    @Test
    void getClientsByPhoneNumber() {
    }

    @Test
    void getClientsByGalleryTitle() {
    }

    @Test
    void getClientByEmail() {
    }

    @Test
    void getClientsByName() {
    }

    // Helper method to create a ClientResponseDTO
    private static ClientResponseDTO resp(Long id, String fullName, String email, String profilePictureUrl, String phoneNumber, String address, Boolean isEnabled,
                                          UserRole userRole, LocalDateTime registrationDate) {
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setId(id);
        dto.setFullName(fullName);
        dto.setEmail(email);
        dto.setProfilePictureUrl(profilePictureUrl);
        dto.setPhoneNumber(phoneNumber);
        dto.setAddress(address);
        dto.setIsEnabled(isEnabled);
        dto.setUserRole(userRole);
        dto.setRegistrationDate(registrationDate);
        return dto;
    }
}