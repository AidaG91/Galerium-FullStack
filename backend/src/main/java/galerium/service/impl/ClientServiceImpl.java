package galerium.service.impl;

import galerium.dto.client.ClientRequestDTO;
import galerium.dto.client.ClientResponseDTO;
import galerium.dto.client.ClientUpdateDTO;
import galerium.enums.UserRole;
import galerium.model.Client;
import galerium.model.Tag;
import galerium.repository.ClientRepository;
import galerium.repository.TagRepository;
import galerium.service.interfaces.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final TagRepository tagRepository;

    public Page<ClientResponseDTO> getClientsPaged(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(this::toResponseDTO);
    }

    @Override
    @Transactional
    public ClientResponseDTO createClient(@Valid ClientRequestDTO clientRequest) {
        if (clientRepository.findByEmail(clientRequest.getEmail()).isPresent()) {
            throw new RuntimeException("A client with this email already exists.");
        }

        Client client = new Client();
        client.setFullName(clientRequest.getFullName());
        client.setEmail(clientRequest.getEmail());
        client.setPassword(clientRequest.getPassword());
        client.setPhoneNumber(clientRequest.getPhoneNumber());
        client.setAddress(clientRequest.getAddress());
        client.setUserRole(UserRole.CLIENT);
        client.setProfilePictureUrl(clientRequest.getProfilePictureUrl());

        // --> 3. Llama al nuevo método para gestionar los tags
        if (clientRequest.getTags() != null) {
            client.setTags(manageTags(clientRequest.getTags()));
        }

        Client savedClient = clientRepository.save(client);

        return toResponseDTO(savedClient);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return toResponseDTO(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public ClientResponseDTO updateClient(Long id, @Valid ClientUpdateDTO clientUpdate) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        if (clientUpdate.getFullName() != null) {
            client.setFullName(clientUpdate.getFullName());
        }
        if (clientUpdate.getEmail() != null) {
            clientRepository.findByEmail(clientUpdate.getEmail()).ifPresent(existingClient -> {
                if (!existingClient.getId().equals(id)) {
                    throw new RuntimeException("Another client with this email already exists.");
                }
            });
            client.setEmail(clientUpdate.getEmail());
        }
        if (clientUpdate.getPhoneNumber() != null) {
            client.setPhoneNumber(clientUpdate.getPhoneNumber());
        }
        if (clientUpdate.getAddress() != null) {
            client.setAddress(clientUpdate.getAddress());
        }
        if (clientUpdate.getProfilePictureUrl() != null) {
            client.setProfilePictureUrl(clientUpdate.getProfilePictureUrl());
        }
        if (clientUpdate.getPassword() != null && clientUpdate.getPassword().length() >= 8) {
            client.setPassword(clientUpdate.getPassword());
        }
        if (clientUpdate.getTags() != null) {
            client.setTags(manageTags(clientUpdate.getTags()));
        }

        Client updatedClient = clientRepository.save(client);

        return toResponseDTO(updatedClient);
    }

    @Override
    @Transactional
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> getClientsByPhoneNumber(String phoneNumber) {
        return clientRepository.findByPhoneNumber(phoneNumber)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> getClientsByGalleryTitle(String title) {
        return clientRepository.findByGalleries_TitleContainingIgnoreCase(title)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDTO getClientByEmail(String email) {
        Optional<Client> clientOpt = clientRepository.findByEmail(email);

        Client client = clientOpt.orElseThrow(() -> new RuntimeException("Client not found with email: " + email));

        return toResponseDTO(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> getClientsByName(String name) {
        return clientRepository.findByFullNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientResponseDTO> searchClientsPaged(String query, Pageable pageable) {
        return clientRepository.searchClientsPaged(query, pageable)
                .map(this::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> searchClients(String query) {
        List<Client> results = clientRepository.searchClients(query);
        return results.stream()
                .map(this::toResponseDTO)
                .toList();
    }



    private Set<Tag> manageTags(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return null; // O un Set vacío, dependiendo de tu lógica de negocio
        }
        return tagNames.stream()
                .map(String::trim) // Limpia espacios en blanco
                .map(tagName -> tagRepository.findByName(tagName)
                        // Si el tag existe, lo devuelve. Si no, crea uno nuevo y lo guarda.
                        .orElseGet(() -> tagRepository.save(new Tag(tagName))))
                .collect(Collectors.toSet());
    }

    // Helper method to map Client entity to ClientResponseDTO
    private ClientResponseDTO toResponseDTO(Client client) {
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setId(client.getId());
        dto.setFullName(client.getFullName());
        dto.setEmail(client.getEmail());
        dto.setPhoneNumber(client.getPhoneNumber());
        dto.setAddress(client.getAddress());
        dto.setIsEnabled(client.isEnabled());
        dto.setUserRole(client.getUserRole());
        dto.setProfilePictureUrl(client.getProfilePictureUrl());
        dto.setRegistrationDate(client.getRegistrationDate());
       /* TODO - Add internal notes field to ClientResponseDTO if needed
           if (internalNotes && client.getInternalNotes() != null) {
            dto.setInternalNotes(client.getInternalNotes());
        }
        */

        if (client.getTags() != null) {
            dto.setTags(client.getTags().stream()
                    .map(Tag::getName)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
