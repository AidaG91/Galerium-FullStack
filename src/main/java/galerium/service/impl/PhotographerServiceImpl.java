package galerium.service.impl;

import galerium.dto.photographer.PhotographerRequestDTO;
import galerium.dto.photographer.PhotographerResponseDTO;
import galerium.dto.photographer.PhotographerUpdateDTO;
import galerium.enums.UserRole;
import galerium.model.Photographer;
import galerium.repository.PhotographerRepository;
import galerium.service.interfaces.PhotographerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotographerServiceImpl implements PhotographerService {

    private final PhotographerRepository photographerRepository;

    @Override
    @Transactional
    public PhotographerResponseDTO createPhotographer(@Valid PhotographerRequestDTO photographerRequest) {
        if (photographerRepository.findByEmail(photographerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("A photographer with this email already exists.");
        }

        Photographer photographer = new Photographer();
        photographer.setFullName(photographerRequest.getFullName());
        photographer.setEmail(photographerRequest.getEmail());
        photographer.setPassword(photographerRequest.getPassword()); // TODO: Encrypt password before saving (e.g., passwordEncoder.encode(...))
        photographer.setUserRole(UserRole.PHOTOGRAPHER);
        photographer.setProfilePictureUrl(photographerRequest.getProfilePictureUrl());
        photographer.setRegistrationDate(java.time.LocalDateTime.now());
        // New photographers are enabled by default

        Photographer savedPhotographer = photographerRepository.save(photographer);

        return toResponseDTO(savedPhotographer);
    }

    @Override
    @Transactional(readOnly = true)
    public PhotographerResponseDTO getPhotographerById(Long id) {
        Photographer photographer = photographerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photographer not found with id: " + id));
        return toResponseDTO(photographer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotographerResponseDTO> getAllPhotographers() {
        return photographerRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public PhotographerResponseDTO updatePhotographer(Long id, @Valid PhotographerUpdateDTO photographerUpdate) {
        Photographer photographer = photographerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photographer not found with id: " + id));

        if (photographerUpdate.getFullName() != null) {
            photographer.setFullName(photographerUpdate.getFullName());
        }
        if (photographerUpdate.getEmail() != null) {
            // Check if the new email is already taken by another photographer
            photographerRepository.findByEmail(photographerUpdate.getEmail()).ifPresent(existingPhotographer -> {
                if (!existingPhotographer.getId().equals(id)) {
                    throw new RuntimeException("A photographer with this email already exists.");
                }
            });
            photographer.setEmail(photographerUpdate.getEmail());
        }
        if (photographerUpdate.getPassword() != null) {
            photographer.setPassword(photographerUpdate.getPassword()); // TODO: Encrypt password before saving
        }
        if (photographerUpdate.getProfilePictureUrl() != null) {
            photographer.setProfilePictureUrl(photographerUpdate.getProfilePictureUrl());
        }

        Photographer updatedPhotographer = photographerRepository.save(photographer);
        return toResponseDTO(updatedPhotographer);

    }

    @Override
    @Transactional
    public void deletePhotographer(Long id) {
        if (!photographerRepository.existsById(id)) {
            throw new RuntimeException("Photographer not found with id: " + id);
        }
        photographerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotographerResponseDTO> getPhotographersByGalleryTitle(String title) {
        return photographerRepository.findByGalleries_TitleContainingIgnoreCase(title)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PhotographerResponseDTO getPhotographerByEmail(String email) {
        Optional<Photographer> photographerOpt = photographerRepository.findByEmail(email);

        Photographer photographer = photographerOpt.orElseThrow(() -> new RuntimeException("Photographer not found with email: " + email));

        return toResponseDTO(photographer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotographerResponseDTO> getPhotographersByName(String name) {
        return photographerRepository.findByFullNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Helper method to convert Photographer entity to PhotographerResponseDTO
    private PhotographerResponseDTO toResponseDTO(Photographer photographer) {
        PhotographerResponseDTO dto = new PhotographerResponseDTO();
        dto.setId(photographer.getId());
        dto.setFullName(photographer.getFullName());
        dto.setEmail(photographer.getEmail());
        dto.setIsEnabled(photographer.isEnabled());
        dto.setUserRole(photographer.getUserRole());
        dto.setProfilePictureUrl(photographer.getProfilePictureUrl());
        dto.setRegistrationDate(photographer.getRegistrationDate());

        return dto;
    }
}
