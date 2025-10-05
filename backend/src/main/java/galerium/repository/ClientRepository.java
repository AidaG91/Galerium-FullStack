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

    @Query(value = "SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.tags",
            countQuery = "SELECT COUNT(c) FROM Client c")
    @Override
    Page<Client> findAll(Pageable pageable);

    @Query("""
              SELECT c FROM Client c
              WHERE LOWER(c.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
                 OR LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%'))
                 OR REPLACE(c.phoneNumber, ' ', '') LIKE REPLACE(LOWER(CONCAT('%', :query, '%')), ' ', '')
                 OR LOWER(c.address) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    List<Client> searchClients(@Param("query") String query);

    @Query(value = """
          SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.tags
          WHERE LOWER(c.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
             OR LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%'))
             OR REPLACE(c.phoneNumber, ' ', '') LIKE REPLACE(LOWER(CONCAT('%', :query, '%')), ' ', '')
             OR LOWER(c.address) LIKE LOWER(CONCAT('%', :query, '%'))
        """,
            countQuery = """
          SELECT COUNT(c) FROM Client c
          WHERE LOWER(c.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
             OR LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%'))
             OR REPLACE(c.phoneNumber, ' ', '') LIKE REPLACE(LOWER(CONCAT('%', :query, '%')), ' ', '')
             OR LOWER(c.address) LIKE LOWER(CONCAT('%', :query, '%'))
        """)
    Page<Client> searchClientsPaged(@Param("query") String query, Pageable pageable);


    @Query(value = "SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.tags t WHERE c.id IN (SELECT c2.id FROM Client c2 JOIN c2.tags t2 WHERE t2.name IN :tags GROUP BY c2.id HAVING COUNT(t2.id) = :tagCount)",
            countQuery = "SELECT COUNT(DISTINCT c.id) FROM Client c JOIN c.tags t WHERE t.name IN :tags GROUP BY c.id HAVING COUNT(t.id) = :tagCount")
    Page<Client> findByAllTags(@Param("tags") List<String> tags, @Param("tagCount") Long tagCount, Pageable pageable);

    boolean existsByTagsName(String tagName);
}
