package galerium.repository;

import galerium.model.Client;
import galerium.model.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByPhoneNumber(String phoneNumber);
    List<Client> findByGalleriesContaining(Gallery gallery);
}
