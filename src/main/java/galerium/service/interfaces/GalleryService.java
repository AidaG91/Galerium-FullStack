package galerium.service.interfaces;

import galerium.dto.gallery.GalleryRequestDTO;
import galerium.dto.gallery.GalleryResponseDTO;
import galerium.dto.gallery.GalleryUpdateDTO;
import galerium.model.Gallery;

import java.time.LocalDate;
import java.util.List;

public interface GalleryService {

    GalleryResponseDTO createGallery(GalleryRequestDTO galleryRequestDTO);

    GalleryResponseDTO getGalleryById(Long id);

    List<GalleryResponseDTO> getAllGalleries();

    GalleryResponseDTO updateGallery(Long id, GalleryUpdateDTO galleryUpdateDTO);

    void deleteGallery(Long id);

    List<GalleryResponseDTO> getGalleriesByPhotographerId(Long photographerId);

    List<GalleryResponseDTO> getGalleriesByClientId(Long clientId);

    List<GalleryResponseDTO> getGalleriesByTitle(String title);

    List<GalleryResponseDTO> getGalleriesByDateRange(LocalDate startDate, LocalDate endDate);

   Gallery getGalleryEntityById(Long id);

}
