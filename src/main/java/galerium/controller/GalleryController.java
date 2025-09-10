package galerium.controller;

import galerium.dto.gallery.GalleryRequestDTO;
import galerium.dto.gallery.GalleryResponseDTO;
import galerium.dto.gallery.GalleryUpdateDTO;
import galerium.service.interfaces.GalleryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/galleries")
@Validated
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;

    @PostMapping
    @Operation(summary = "Create a new gallery", description = "Create a new gallery and return the created gallery with a Location header.")
    public ResponseEntity<GalleryResponseDTO> createGallery(@Valid @RequestBody GalleryRequestDTO body) {
        GalleryResponseDTO created = galleryService.createGallery(body);
        URI location = URI.create("/api/galleries/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update gallery by ID", description = "Update an existing gallery's information by their ID.")
    public ResponseEntity<GalleryResponseDTO> updateGallery(@PathVariable @Positive Long id,
                                                              @Valid @RequestBody GalleryUpdateDTO body) {
          return ResponseEntity.ok(galleryService.updateGallery(id, body));
     }

    @GetMapping
    @Operation(summary = "Get all galleries", description = "Retrieve a list of all galleries in the system.")
    public ResponseEntity<List<GalleryResponseDTO>> getAllGalleries() {
        return ResponseEntity.ok(galleryService.getAllGalleries());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get gallery by ID", description = "Retrieve the details of a gallery by their ID.")
    public ResponseEntity<GalleryResponseDTO> getGalleryById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(galleryService.getGalleryById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete gallery by ID", description = "Delete a gallery by their ID.")
    public ResponseEntity<Void> deleteGallery(@PathVariable @Positive Long id) {
        galleryService.deleteGallery(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-photographer/{id}")
    @Operation(summary = "Get galleries by photographer ID", description = "Retrieve a list of galleries associated with a specific photographer ID.")
    public ResponseEntity<List<GalleryResponseDTO>> getGalleriesByPhotographerId(@PathVariable @Positive Long photographerId) {
        return ResponseEntity.ok(galleryService.getGalleriesByPhotographerId(photographerId));
    }

    @GetMapping("/by-client/{id}")
    @Operation(summary = "Get galleries by client ID", description = "Retrieve a list of galleries associated with a specific client ID.")
    public ResponseEntity<List<GalleryResponseDTO>> getGalleriesByClientId(@PathVariable @Positive Long clientId) {
        return ResponseEntity.ok(galleryService.getGalleriesByClientId(clientId));
    }

    @GetMapping("/by-title/{title}")
    @Operation(summary = "Get galleries by title", description = "Retrieve a list of galleries filtered by their title.")
    public ResponseEntity<List<GalleryResponseDTO>> getGalleriesByTitle(@PathVariable String title) {
        return ResponseEntity.ok(galleryService.getGalleriesByTitle(title));
    }

    @GetMapping("/by-date-range")
    @Operation(summary = "Get galleries by date range", description = "Retrieve a list of galleries created within a specific date range.")
    public ResponseEntity<List<GalleryResponseDTO>> getGalleriesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(galleryService.getGalleriesByDateRange(startDate, endDate));
    }

}
