package galerium.service.interfaces;

import galerium.dto.photographer.PhotographerRequestDTO;
import galerium.dto.photographer.PhotographerResponseDTO;
import galerium.dto.photographer.PhotographerUpdateDTO;

import java.util.List;

public interface PhotographerService {

    PhotographerResponseDTO createPhotographer(PhotographerRequestDTO photographerRequestDTO);

    PhotographerResponseDTO getPhotographerById(Long id);

    List<PhotographerResponseDTO> getAllPhotographers();

    PhotographerResponseDTO updatePhotographer(Long id, PhotographerUpdateDTO photographerUpdateDTO);

    void deletePhotographer(Long id);

    List<PhotographerResponseDTO> getPhotographersByGalleryTitle(String title);

    PhotographerResponseDTO getPhotographerByEmail(String email);

    List<PhotographerResponseDTO> getPhotographersByName(String name);
}
