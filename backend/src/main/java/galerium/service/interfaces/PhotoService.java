package galerium.service.interfaces;

import galerium.dto.photo.PhotoRequestDTO;
import galerium.dto.photo.PhotoResponseDTO;
import galerium.dto.photo.PhotoUpdateDTO;

import java.util.List;

public interface PhotoService {
    PhotoResponseDTO createPhoto(PhotoRequestDTO photoRequestDTO);

    PhotoResponseDTO getPhotoById(Long id);

    List<PhotoResponseDTO> getAllPhotos();

    PhotoResponseDTO updatePhoto(Long id, PhotoUpdateDTO photoUpdateDTO);

    void deletePhoto(Long id);

    List<PhotoResponseDTO> getPhotosByGalleryId(Long galleryId);

    List<PhotoResponseDTO> getFavoritePhotos();

}
