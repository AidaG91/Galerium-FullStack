package galerium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import galerium.config.TestSecurityConfig;
import galerium.dto.client.ClientRequestDTO;
import galerium.dto.client.ClientResponseDTO;
import galerium.dto.client.ClientUpdateDTO;
import galerium.enums.UserRole;
import galerium.service.interfaces.ClientService;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = ClientController.class)
class ClientControllerTest {

    private static final String BASE = "/api/clients";
    private static final String ID = BASE + "/{id}";
    private static final String EMAIL = BASE + "/email/";
    private static final String PHONE = BASE + "/phone/";
    private static final String NAME = BASE + "/name/";
    private static final String GALLERY = BASE + "/gallery/";


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

        var result = mvc.perform(post(BASE)
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
    @DisplayName("POST /api/clients -> 400 BAD REQUEST (missing fields)")
    void createClient_missingFields() throws Exception {
        var req = new ClientRequestDTO();

        mvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ------- TESTING PUT /api/clients/{id} -------
    @Test
    @DisplayName("PUT /api/clients/{id} -> 200 OK")
    void updateClient_ok() throws Exception {
        var req = new ClientUpdateDTO();
        req.setFullName("John Doe");
        req.setEmail("johndoe@mail.com");
        req.setPhoneNumber("+1234567890");
        req.setAddress("123 Main St, Anytown, USA");
        req.setIsEnabled(true);
        req.setProfilePictureUrl("http://example.com/pic.jpg");
        req.setPassword("NewPassword123!");

        var updated = resp(1L, "John Doe", "johndoe@mail.com", "http://example.com/pic.jpg", "+1234567890","123 Main St, Anytown, USA", true, UserRole.CLIENT, LocalDateTime.now());
        when(clientService.updateClient(eq(1L), any())).thenReturn(updated);

        mvc.perform(put(BASE + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@mail.com"))
                .andExpect(jsonPath("$.profilePictureUrl").value("http://example.com/pic.jpg"))
                .andExpect(jsonPath("$.phoneNumber").value("+1234567890"))
                .andExpect(jsonPath("$.address").value("123 Main St, Anytown, USA"))
                .andExpect(jsonPath("$.isEnabled").value(true))
                .andExpect(jsonPath("$.userRole").value("CLIENT"));

        ArgumentCaptor<ClientUpdateDTO> captor = ArgumentCaptor.forClass(ClientUpdateDTO.class);

        verify(clientService).updateClient(eq(1L), captor.capture());

        assertThat(captor.getValue().getPassword()).isEqualTo("NewPassword123!");
    }

    @Test
    @DisplayName("PUT /api/clients/{id} -> 400 BAD REQUEST (missing required fields)")
    void updateClient_missingFields() throws Exception {
        var req = new ClientUpdateDTO();

        mvc.perform(put(ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/clients/{id} -> 400 BAD REQUEST (invalid email format)")
    void updateClient_invalidEmail() throws Exception {
        var req = new ClientUpdateDTO();
        req.setFullName("Nombre válido");
        req.setEmail("correoSinArroba");
        req.setPhoneNumber("+34123456789");
        req.setAddress("Calle Falsa 123");
        req.setIsEnabled(true);
        req.setPassword("Password123!");

        mvc.perform(put(ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/clients -> 409 CONFLICT (duplicate email)")
    void createClient_duplicateEmail() throws Exception {
        var req = new ClientRequestDTO();
        req.setFullName("John Doe");
        req.setEmail("johndoe@mail.com");
        req.setPassword("Password123!");
        req.setPhoneNumber("+1234567890");
        req.setAddress("123 Main St");

        when(clientService.createClient(any()))
                .thenThrow(new RuntimeException("A client with this email already exists."));

        mvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(content().string("A client with this email already exists."));
    }


    // ------- TESTING GET /api/clients -------
    @Test
    @DisplayName("GET /api/clients -> 200 OK")
    void getAllClients_ok() throws Exception {
        var c1 = resp(1L, "Lola Flores", "lolaflores@email.com", null, "+34123456789", "Calle Falsa 123, Madrid, España", true, UserRole.CLIENT, LocalDateTime.now());
        var c2 = resp(2L, "Juan Palomo", "juanpalomo@email.com", null, "+34111222333", "Avenida Siempre Viva 742, Barcelona, España", true, UserRole.CLIENT, LocalDateTime.now());

        when(clientService.getAllClients()).thenReturn(java.util.List.of(c1, c2));

        mvc.perform(get(BASE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Lola Flores"))
                .andExpect(jsonPath("$[0].email").value("lolaflores@email.com"))
                .andExpect(jsonPath("$[0].phoneNumber").value("+34123456789"))
                .andExpect(jsonPath("$[0].address").value("Calle Falsa 123, Madrid, España"))
                .andExpect(jsonPath("$[0].isEnabled").value(true))
                .andExpect(jsonPath("$[0].userRole").value("CLIENT"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].fullName").value("Juan Palomo"))
                .andExpect(jsonPath("$[1].email").value("juanpalomo@email.com"))
                .andExpect(jsonPath("$[1].phoneNumber").value("+34111222333"))
                .andExpect(jsonPath("$[1].address").value("Avenida Siempre Viva 742, Barcelona, España"))
                .andExpect(jsonPath("$[1].isEnabled").value(true))
                .andExpect(jsonPath("$[1].userRole").value("CLIENT"));

        verify(clientService).getAllClients();
    }

    // ------- TESTING GET /api/clients/{id} -------
    @Test
    @DisplayName("GET /api/clients/{id} -> 200 OK")
    void getClientById_ok() throws Exception {
        var c1 = resp(1L, "Lola Flores", "lolaflores@email.com", null, "+34123456789", "Calle Falsa 123, Madrid, España", true, UserRole.CLIENT, LocalDateTime.now());

        when(clientService.getClientById(1L)).thenReturn(c1);

        mvc.perform(get(ID, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Lola Flores"))
                .andExpect(jsonPath("$.email").value("lolaflores@email.com"))
                .andExpect(jsonPath("$.phoneNumber").value("+34123456789"))
                .andExpect(jsonPath("$.address").value("Calle Falsa 123, Madrid, España"))
                .andExpect(jsonPath("$.isEnabled").value(true))
                .andExpect(jsonPath("$.userRole").value("CLIENT"));

        verify(clientService).getClientById(1L);
    }

    @Test
    @DisplayName("GET /api/clients/{id} -> 404 NOT FOUND")
    void getClientById_notFound() throws Exception {
        when(clientService.getClientById(999L)).thenThrow(new EntityNotFoundException("Client not found"));

        mvc.perform(get(ID, 999))
                .andExpect(status().isNotFound());

        verify(clientService).getClientById(999L);
    }

    // ------- TESTING GET /api/clients/phone/{phoneNumber} -------
    @Test
    @DisplayName("GET /api/clients/phone/{phoneNumber} -> 200 OK")
    void getClientsByPhoneNumber_ok() throws Exception{
        var c1 = resp(1L, "Lola Flores", "lolaflores@email.com", null, "+34123456789", "Calle Falsa 123, Madrid, España", true, UserRole.CLIENT, LocalDateTime.now());

        when(clientService.getClientsByPhoneNumber("+34123456789")).thenReturn(java.util.List.of(c1));

        mvc.perform(get(PHONE + "/{phoneNumber}", "+34123456789")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Lola Flores"))
                .andExpect(jsonPath("$[0].email").value("lolaflores@email.com"))
                .andExpect(jsonPath("$[0].phoneNumber").value("+34123456789"))
                .andExpect(jsonPath("$[0].address").value("Calle Falsa 123, Madrid, España"))
                .andExpect(jsonPath("$[0].isEnabled").value(true))
                .andExpect(jsonPath("$[0].userRole").value("CLIENT"));

        verify(clientService).getClientsByPhoneNumber("+34123456789");
    }

    @Test
    @DisplayName("GET /api/clients/phone/{phoneNumber} -> 404 NOT FOUND")
    void getClientsByPhoneNumber_notFound() throws Exception {
        when(clientService.getClientsByPhoneNumber("+34000000000"))
                .thenThrow(new EntityNotFoundException("No clients found with phone number"));

        mvc.perform(get(PHONE + "/{phoneNumber}", "+34000000000"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No clients found with phone number"));

        verify(clientService).getClientsByPhoneNumber("+34000000000");
    }


    // ------- TESTING GET /api/clients/gallery/{title} -------
    @Test
    @DisplayName("GET /api/clients/gallery/{title} -> 200 OK")
    void getClientsByGalleryTitle_ok() throws Exception {
        var c1 = resp(1L, "Lola Flores", "lolaflores@email.com", null, "+34123456789", "Calle Falsa 123, Madrid, España", true, UserRole.CLIENT, LocalDateTime.now());

        when(clientService.getClientsByGalleryTitle("Modern Art")).thenReturn(java.util.List.of(c1));
        mvc.perform(get(GALLERY + "/{title}", "Modern Art")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Lola Flores"))
                .andExpect(jsonPath("$[0].email").value("lolaflores@email.com"))
                .andExpect(jsonPath("$[0].phoneNumber").value("+34123456789"))
                .andExpect(jsonPath("$[0].address").value("Calle Falsa 123, Madrid, España"))
                .andExpect(jsonPath("$[0].isEnabled").value(true))
                .andExpect(jsonPath("$[0].userRole").value("CLIENT"));

        verify(clientService).getClientsByGalleryTitle("Modern Art");
    }

    @Test
    @DisplayName("GET /api/clients/gallery/{title} -> 404 NOT FOUND")
    void getClientsByGalleryTitle_notFound() throws Exception {
        when(clientService.getClientsByGalleryTitle("Galería Fantasma"))
                .thenThrow(new EntityNotFoundException("No clients found for gallery"));

        mvc.perform(get(GALLERY + "/{title}", "Galería Fantasma"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No clients found for gallery"));

        verify(clientService).getClientsByGalleryTitle("Galería Fantasma");
    }

    // ------- TESTING GET /api/clients/email/{email} -------
    @Test
    @DisplayName("GET /api/clients/email/{email} -> 200 OK")
    void getClientByEmail_ok() throws Exception {
        var c1 = resp(1L, "Lola Flores", "lolaflores@email.com", null, "+34123456789", "Calle Falsa 123, Madrid, España", true, UserRole.CLIENT, LocalDateTime.now());

        when(clientService.getClientByEmail("lolaflores@email.com")).thenReturn(c1);

        mvc.perform(get(EMAIL + "/{email}", "lolaflores@email.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Lola Flores"))
                .andExpect(jsonPath("$.email").value("lolaflores@email.com"))
                .andExpect(jsonPath("$.phoneNumber").value("+34123456789"))
                .andExpect(jsonPath("$.address").value("Calle Falsa 123, Madrid, España"))
                .andExpect(jsonPath("$.isEnabled").value(true))
                .andExpect(jsonPath("$.userRole").value("CLIENT"));

        verify(clientService).getClientByEmail("lolaflores@email.com");
    }

    @Test
    @DisplayName("GET /api/clients/email/{email} -> 404 NOT FOUND")
    void getClientByEmail_notFound() throws Exception {
        when(clientService.getClientByEmail("noexiste@email.com"))
                .thenThrow(new EntityNotFoundException("Client not found"));

        mvc.perform(get(EMAIL + "/{email}", "noexiste@email.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Client not found"));

        verify(clientService).getClientByEmail("noexiste@email.com");
    }

    // ------- TESTING GET /api/clients/name/{name} -------
    @Test
    @DisplayName("GET /api/clients/name/{name} -> 200 OK")
    void getClientsByName_ok() throws Exception {
        var c1 = resp(1L, "Lola Flores", "lolaflores@email.com", null, "+34123456789", "Calle Falsa 123, Madrid, España", true, UserRole.CLIENT, LocalDateTime.now());

        when(clientService.getClientsByName("Lola")).thenReturn(java.util.List.of(c1));

        mvc.perform(get(NAME + "/{name}", "Lola")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Lola Flores"))
                .andExpect(jsonPath("$[0].email").value("lolaflores@email.com"))
                .andExpect(jsonPath("$[0].phoneNumber").value("+34123456789"))
                .andExpect(jsonPath("$[0].address").value("Calle Falsa 123, Madrid, España"))
                .andExpect(jsonPath("$[0].isEnabled").value(true))
                .andExpect(jsonPath("$[0].userRole").value("CLIENT"));

        verify(clientService).getClientsByName("Lola");
    }

    @Test
    @DisplayName("GET /api/clients/name/{name} -> 404 NOT FOUND")
    void getClientsByName_notFound() throws Exception {
        when(clientService.getClientsByName("NombreFantasma"))
                .thenThrow(new EntityNotFoundException("No clients found with name"));

        mvc.perform(get(NAME + "/{name}", "NombreFantasma"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No clients found with name"));

        verify(clientService).getClientsByName("NombreFantasma");
    }

    // ------- TESTING DELETE /api/clients/{id} -------
    @Test
    @DisplayName("DELETE /api/clients/{id} -> 204 NO CONTENT")
    void deleteClient_noContent() throws Exception {
        doNothing().when(clientService).deleteClient(1L);

        mvc.perform(delete(ID, 1))
                .andExpect(status().isNoContent());

        verify(clientService).deleteClient(1L);
    }

    @Test
    @DisplayName("DELETE /api/clients/{id} -> 404 NOT FOUND")
    void deleteClient_notFound() throws Exception {
        doThrow(new EntityNotFoundException("Client not found")).when(clientService).deleteClient(999L);

        mvc.perform(delete(ID, 999))
                .andExpect(status().isNotFound());

        verify(clientService).deleteClient(999L);
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