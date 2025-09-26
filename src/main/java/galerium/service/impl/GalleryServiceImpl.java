package galerium.service.impl;

import galerium.dto.gallery.GalleryRequestDTO;
import galerium.dto.gallery.GalleryResponseDTO;
import galerium.dto.gallery.GalleryUpdateDTO;
import galerium.dto.photo.PhotoResponseDTO;
import galerium.model.Client;
import galerium.model.Gallery;
import galerium.model.Photo;
import galerium.model.Photographer;
import galerium.repository.ClientRepository;
import galerium.repository.GalleryRepository;
import galerium.repository.PhotographerRepository;
import galerium.service.interfaces.GalleryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {

    // Repositories to interact with the database
    private final GalleryRepository galleryRepository;
    private final PhotographerRepository photographerRepository;
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public GalleryResponseDTO createGallery(@Valid GalleryRequestDTO galleryRequestDTO) {
        Gallery gallery = new Gallery();
        // Set the associated Photographer and Client entities
        // These methods should fetch the entities based on the provided IDs
        Photographer photographer = photographerRepository.findById(galleryRequestDTO.getPhotographerId())
                .orElseThrow(() -> new RuntimeException("Photographer not found"));

        Client client = clientRepository.findById(galleryRequestDTO.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Assign fields from DTO to entity
        gallery.setTitle(galleryRequestDTO.getTitle());
        gallery.setDate(galleryRequestDTO.getDate());
        gallery.setDescription(galleryRequestDTO.getDescription());
        gallery.setPhotographer(photographer);
        gallery.setClient(client);

        Gallery savedGallery = galleryRepository.save(gallery);
        return toResponseDto(savedGallery);
    }

    @Override
    @Transactional(readOnly = true)
    public GalleryResponseDTO getGalleryById(Long id) {
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gallery not found with id: " + id));
        return toResponseDto(gallery);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GalleryResponseDTO> getAllGalleries() {
        return galleryRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GalleryResponseDTO updateGallery(Long id, @Valid GalleryUpdateDTO galleryUpdateDTO) {
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gallery not found with id: " + id));

        if (galleryUpdateDTO.getTitle() != null) {
            gallery.setTitle(galleryUpdateDTO.getTitle());
        }
        if (galleryUpdateDTO.getDate() != null) {
            gallery.setDate(galleryUpdateDTO.getDate());
        }
        if (galleryUpdateDTO.getDescription() != null) {
            gallery.setDescription(galleryUpdateDTO.getDescription());
        }

        Gallery updatedGallery = galleryRepository.save(gallery);
        return toResponseDto(updatedGallery);
    }

    @Override
    @Transactional
    public void deleteGallery(Long id) {
        if (!galleryRepository.existsById(id)) {
            throw new RuntimeException("Gallery not found with id: " + id);
        }
        galleryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GalleryResponseDTO> getGalleriesByPhotographerId(Long photographerId) {
        return galleryRepository.findByPhotographerId(photographerId)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GalleryResponseDTO> getGalleriesByClientId(Long clientId) {
        return galleryRepository.findByClientId(clientId)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GalleryResponseDTO> getGalleriesByTitle(String title) {
        return galleryRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GalleryResponseDTO> getGalleriesByDateRange(LocalDate startDate, LocalDate endDate) {
        return galleryRepository.findByDateBetween(startDate, endDate)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Gallery getGalleryEntityById(Long id) {
        return galleryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gallery not found with id: " + id));
    }


    // Helper method to convert Gallery entity to GalleryResponseDTO
    private GalleryResponseDTO toResponseDto(Gallery gallery) {
        GalleryResponseDTO dto = new GalleryResponseDTO();
        dto.setId(gallery.getId());
        dto.setTitle(gallery.getTitle());
        dto.setDate(gallery.getDate());
        dto.setDescription(gallery.getDescription());
        dto.setPhotographerId(gallery.getPhotographer().getId());
        dto.setClientId(gallery.getClient().getId());

        // Convert associated photos in the gallery to PhotoResponseDTOs
        List<PhotoResponseDTO> photoDTOs = gallery.getPhotos() == null ? List.of()
                : gallery.getPhotos().stream()
                .map(this::toPhotoResponseDto)
                .collect(Collectors.toList());

        dto.setPhotos(photoDTOs);

        return dto;
    }

    // Helper method to convert Photo entity to PhotoResponseDTO to include in GalleryResponseDTO
    private PhotoResponseDTO toPhotoResponseDto(Photo photo) {
        PhotoResponseDTO dto = new PhotoResponseDTO();
        dto.setId(photo.getId());
        dto.setGalleryId(photo.getGallery().getId());
        dto.setUrl(photo.getUrl());
        dto.setTitle(photo.getTitle());
        dto.setDescription(photo.getDescription());
        dto.setClientComment(photo.getClientComment());
        dto.setFavorite(photo.isFavorite());
        return dto;
    }

}

