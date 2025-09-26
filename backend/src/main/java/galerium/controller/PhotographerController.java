package galerium.controller;

import galerium.dto.photographer.PhotographerRequestDTO;
import galerium.dto.photographer.PhotographerResponseDTO;
import galerium.dto.photographer.PhotographerUpdateDTO;
import galerium.service.interfaces.PhotographerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/photographers")
@Validated
@RequiredArgsConstructor
public class PhotographerController {

    private final PhotographerService photographerService;

    @PostMapping
    @Operation(summary = "Create a new photographer", description = "Create a new photographer and return the created photographer with a Location header.")
    public ResponseEntity<PhotographerResponseDTO> createPhotographer(@Valid @RequestBody PhotographerRequestDTO body) {
        PhotographerResponseDTO created = photographerService.createPhotographer(body);
        URI location = URI.create("/api/photographers/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update photographer by ID", description = "Update an existing photographer's information by their ID.")
    public ResponseEntity<PhotographerResponseDTO> updatePhotographer(@PathVariable @Positive Long id,
                                                                     @Valid @RequestBody PhotographerUpdateDTO body) {
        return ResponseEntity.ok(photographerService.updatePhotographer(id, body));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get photographer by ID", description = "Retrieve the details of a photographer by their unique ID. The ID must be positive.")
    public ResponseEntity<PhotographerResponseDTO> getPhotographerById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(photographerService.getPhotographerById(id));
    }

    @GetMapping
    @Operation(summary = "Get all photographers", description = "Retrieve a list of all photographers in the system.")
    public ResponseEntity<List<PhotographerResponseDTO>> getAllPhotographers() {
        return ResponseEntity.ok(photographerService.getAllPhotographers());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete photographer by ID", description = "Delete a photographer by their unique positive ID.")
    public ResponseEntity<Void> deletePhotographer(@PathVariable @Positive Long id) {
        photographerService.deletePhotographer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get photographer by email", description = "Retrieve the details of a photographer by their unique email address.")
    public ResponseEntity<PhotographerResponseDTO> getPhotographerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(photographerService.getPhotographerByEmail(email));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get photographers by name", description = "Retrieve a list of photographers filtered by their name.")
    public ResponseEntity<List<PhotographerResponseDTO>> getPhotographersByName(@PathVariable String name) {
        return ResponseEntity.ok(photographerService.getPhotographersByName(name));
    }

    @GetMapping("/gallery/{title}")
    @Operation(summary = "Get photographers by gallery title", description = "Retrieve a list of photographers associated with a specific gallery title.")
    public ResponseEntity<List<PhotographerResponseDTO>> getPhotographersByGalleryTitle(@PathVariable String title) {
        return ResponseEntity.ok(photographerService.getPhotographersByGalleryTitle(title));
    }
}
