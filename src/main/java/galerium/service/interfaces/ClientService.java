package galerium.service.interfaces;

import galerium.dto.client.ClientRequestDTO;
import galerium.dto.client.ClientResponseDTO;
import galerium.dto.client.ClientUpdateDTO;

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

}
