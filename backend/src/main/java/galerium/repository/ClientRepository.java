package galerium.repository;

import galerium.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByPhoneNumber(String phoneNumber);
    List<Client> findByGalleries_TitleContainingIgnoreCase(String title);
    Optional<Client> findByEmail(String email);
    List<Client> findByFullNameContainingIgnoreCase(String name);

    @Query("""
              SELECT c FROM Client c
              WHERE LOWER(c.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
                 OR LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%'))
                 OR LOWER(c.phoneNumber) LIKE LOWER(CONCAT('%', :query, '%'))
                 OR LOWER(c.address) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    List<Client> searchClients(@Param("query") String query);

    @Query("""
              SELECT c FROM Client c
              WHERE LOWER(c.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
                 OR LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%'))
                 OR LOWER(c.phoneNumber) LIKE LOWER(CONCAT('%', :query, '%'))
                 OR LOWER(c.address) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    Page<Client> searchClientsPaged(@Param("query") String query, Pageable pageable);

    Page<Client> findByTags_NameIgnoreCase(String tagName, Pageable pageable);

}
