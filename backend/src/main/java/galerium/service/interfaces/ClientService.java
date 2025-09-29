package galerium.service.interfaces;

import galerium.dto.client.ClientRequestDTO;
import galerium.dto.client.ClientResponseDTO;
import galerium.dto.client.ClientUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {

    ClientResponseDTO createClient(ClientRequestDTO clientRequestDTO);

    ClientResponseDTO getClientById(Long id);

    List<ClientResponseDTO> getAllClients();

    ClientResponseDTO updateClient(Long id, ClientUpdateDTO clientUpdateDTO);

    void deleteClient(Long id);

    List<ClientResponseDTO> getClientsByPhoneNumber(String phoneNumber);

    List<ClientResponseDTO> getClientsByGalleryTitle(String title);

    ClientResponseDTO getClientByEmail(String email);

    List<ClientResponseDTO> getClientsByName(String name);

    Page<ClientResponseDTO> getClientsPaged(Pageable pageable);

    List<ClientResponseDTO> searchClients(String query);

    Page<ClientResponseDTO> searchClientsPaged(String query, Pageable pageable);

}
