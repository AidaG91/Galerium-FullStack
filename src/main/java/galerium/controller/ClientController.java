package galerium.controller;

import galerium.dto.client.ClientRequestDTO;
import galerium.dto.client.ClientResponseDTO;
import galerium.dto.client.ClientUpdateDTO;
import galerium.service.interfaces.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
@Validated
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @Operation(summary = "Create a new client", description = "Create a new client and return the created client with a Location header.")
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO body) {
        ClientResponseDTO created = clientService.createClient(body);
        URI location = URI.create("/api/clients/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update client by ID", description = "Update an existing client's information by their ID.")
    public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable @Positive Long id,
                                                          @Valid @RequestBody ClientUpdateDTO body) {
        return ResponseEntity.ok(clientService.updateClient(id, body));
    }

    @GetMapping
    @Operation(summary = "Get all clients", description = "Retrieve a list of all clients in the system.")
    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID", description = "Retrieve the details of a client by their unique ID. The ID must be positive.")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete client by ID", description = "Delete a client by their unique positive ID.")
    public ResponseEntity<Void> deleteClient(@PathVariable @Positive Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/phone/{phoneNumber}")
    @Operation(summary = "Get clients by phone number",
            description = "Retrieve a list of clients filtered by their phone number.")
    public ResponseEntity<List<ClientResponseDTO>> getClientsByPhoneNumber(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(clientService.getClientsByPhoneNumber(phoneNumber));
    }

    @GetMapping("/gallery/{title}")
    @Operation(summary = "Get clients by gallery title",
            description = "Retrieve a list of clients associated with a specific gallery title.")
    public ResponseEntity<List<ClientResponseDTO>> getClientsByGalleryTitle(@PathVariable String title) {
        return ResponseEntity.ok(clientService.getClientsByGalleryTitle(title));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get client by email",
            description = "Retrieve a client's details by their email address.")
    public ResponseEntity<ClientResponseDTO> getClientByEmail(@PathVariable String email) {
        return ResponseEntity.ok(clientService.getClientByEmail(email));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get clients by name",
            description = "Retrieve a list of clients filtered by their name.")
    public ResponseEntity<List<ClientResponseDTO>> getClientsByName(@PathVariable String name) {
        return ResponseEntity.ok(clientService.getClientsByName(name));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
