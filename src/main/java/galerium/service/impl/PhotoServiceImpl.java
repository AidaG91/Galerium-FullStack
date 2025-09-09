package galerium.service.impl;

import galerium.dto.photo.PhotoRequestDTO;
import galerium.dto.photo.PhotoResponseDTO;
import galerium.dto.photo.PhotoUpdateDTO;
import galerium.model.Photo;
import galerium.repository.PhotoRepository;
import galerium.service.interfaces.GalleryService;
import galerium.service.interfaces.PhotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    private final GalleryService galleryService;

    @Override
    @Transactional
    public PhotoResponseDTO createPhoto(@Valid PhotoRequestDTO photoRequestDTO) {
        Photo photo = new Photo();
        // Assign fields from DTO to entity
        photo.setTitle(photoRequestDTO.getTitle());
        photo.setUrl(photoRequestDTO.getUrl());
        photo.setDescription(photoRequestDTO.getDescription());
        photo.setClientComment(photoRequestDTO.getClientComment());
        photo.setFavorite(photoRequestDTO.isFavorite());

        // Set the associated Gallery entity
        photo.setGallery(galleryService.getGalleryEntityById(photoRequestDTO.getGalleryId()));

        Photo savedPhoto = photoRepository.save(photo);

        return toResponseDto(savedPhoto);
    }

    @Override
    @Transactional(readOnly = true)
    public PhotoResponseDTO getPhotoById(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found with id: " + id));
        return toResponseDto(photo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponseDTO> getAllPhotos() {
        return photoRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PhotoResponseDTO updatePhoto(Long id, @Valid PhotoUpdateDTO photoUpdate) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found with id: " + id));

        if (photoUpdate.getTitle() != null) {
            photo.setTitle(photoUpdate.getTitle());
        }
        if (photoUpdate.getUrl() != null) {
            photo.setUrl(photoUpdate.getUrl());
        }
        if (photoUpdate.getDescription() != null) {
            photo.setDescription(photoUpdate.getDescription());
        }
        if (photoUpdate.getClientComment() != null) {
            photo.setClientComment(photoUpdate.getClientComment());
        }
        if (photoUpdate.getFavorite() != null) {
            photo.setFavorite(photoUpdate.getFavorite());
        }

        Photo updatedPhoto = photoRepository.save(photo);
        return toResponseDto(updatedPhoto);
    }

    @Override
    @Transactional
    public void deletePhoto(Long id) {
        if (!photoRepository.existsById(id)) {
            throw new RuntimeException("Photo not found with id: " + id);
        }
        photoRepository.deleteById(id);
    }

    // Get all photos in a specific gallery
    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponseDTO> getPhotosByGalleryId(Long galleryId) {
        return photoRepository.findByGalleryId(galleryId)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get all favorite photos
    @Override
    @Transactional(readOnly = true)
    public List<PhotoResponseDTO> getFavoritePhotos() {
        return photoRepository.findByFavoriteTrue()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // Helper method to convert Photo entity to PhotoResponseDTO
    private PhotoResponseDTO toResponseDto(Photo photo) {
        PhotoResponseDTO dto = new PhotoResponseDTO();
        dto.setId(photo.getId());
        dto.setTitle(photo.getTitle());
        dto.setUrl(photo.getUrl());
        dto.setFavorite(photo.isFavorite());
        dto.setGalleryId(photo.getGallery().getId());
        dto.setDescription(photo.getDescription());
        dto.setClientComment(photo.getClientComment());
        return dto;
    }

}
