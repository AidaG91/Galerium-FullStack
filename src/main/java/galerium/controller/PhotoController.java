package galerium.controller;

import galerium.dto.photo.PhotoRequestDTO;
import galerium.dto.photo.PhotoResponseDTO;
import galerium.dto.photo.PhotoUpdateDTO;
import galerium.service.interfaces.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/photos")
@Validated
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping
    @Operation(summary = "Create a new photo", description = "Create a new photo and return the created photo with a Location header.")
    public ResponseEntity<PhotoResponseDTO> createPhoto(@Valid @RequestBody PhotoRequestDTO body) {
        PhotoResponseDTO created = photoService.createPhoto(body);
        URI location = URI.create("/api/photos/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update photo by ID", description = "Update an existing photo's information by their ID.")
    public ResponseEntity<PhotoResponseDTO> updatePhoto(@PathVariable @Positive Long id,
                                                        @Valid @RequestBody PhotoUpdateDTO body) {
        return ResponseEntity.ok(photoService.updatePhoto(id, body));
    }

    @GetMapping
    @Operation(summary = "Get all photos", description = "Retrieve a list of all photos in the system.")
    public ResponseEntity<java.util.List<PhotoResponseDTO>> getAllPhotos() {
        return ResponseEntity.ok(photoService.getAllPhotos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get photo by ID", description = "Retrieve the details of a photo by their ID.")
    public ResponseEntity<PhotoResponseDTO> getPhotoById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(photoService.getPhotoById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete photo by ID", description = "Delete a photo by their ID.")
    public ResponseEntity<Void> deletePhoto(@PathVariable @Positive Long id) {
        photoService.deletePhoto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/gallery/{galleryId}")
    @Operation(summary = "Get photos by gallery ID", description = "Retrieve a list of photos by their gallery ID.")
    public ResponseEntity<java.util.List<PhotoResponseDTO>> getPhotosByGalleryId(@PathVariable @Positive Long galleryId) {
        return ResponseEntity.ok(photoService.getPhotosByGalleryId(galleryId));
    }

    @GetMapping("/favorites")
    @Operation(summary = "Get favorite photos", description = "Retrieve a list of favorite photos.")
    public ResponseEntity<java.util.List<PhotoResponseDTO>> getFavoritePhotos() {
        return ResponseEntity.ok(photoService.getFavoritePhotos());
    }
}
