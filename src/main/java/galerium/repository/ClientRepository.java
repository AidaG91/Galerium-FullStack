package galerium.repository;

import galerium.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByPhoneNumber(String phoneNumber);
    List<Client> findByGalleries_TitleContainingIgnoreCase(String title);
    Optional<Client> findByEmail(String email);
    List<Client> findByFullNameContainingIgnoreCase(String name);
}
