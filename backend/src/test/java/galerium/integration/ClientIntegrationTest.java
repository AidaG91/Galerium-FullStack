package galerium.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import galerium.dto.client.ClientRequestDTO;
import galerium.dto.client.ClientUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class ClientIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Test
    @DisplayName("Full CRUD Cycle Integration Test")
    void fullCrudCycle() throws Exception {
        // --- 1. CREATE ---
        ClientRequestDTO createDto = new ClientRequestDTO();
        createDto.setFullName("Integration Test Client");
        createDto.setEmail("integration.test@example.com");
        createDto.setPassword("password123");
        createDto.setPhoneNumber("123456789");

        MvcResult createResult = mvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        String createResponse = createResult.getResponse().getContentAsString();
        Long clientId = om.readTree(createResponse).get("id").asLong();

        // --- 2. READ (GET BY ID) ---
        mvc.perform(get("/api/clients/" + clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Integration Test Client"));

        // --- 3. UPDATE ---
        ClientUpdateDTO updateDto = new ClientUpdateDTO();
        updateDto.setFullName("Updated Client Name");
        updateDto.setEmail("integration.test@example.com");
        updateDto.setPhoneNumber("123456789");

        mvc.perform(put("/api/clients/" + clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Updated Client Name"));

        // --- 4. VERIFY UPDATE ---
        mvc.perform(get("/api/clients/" + clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Updated Client Name"));

        // --- 5. DELETE ---
        mvc.perform(delete("/api/clients/" + clientId))
                .andExpect(status().isNoContent());

        // --- 6. VERIFY DELETE ---
        mvc.perform(get("/api/clients/" + clientId))
                .andExpect(status().isNotFound());
    }
}
