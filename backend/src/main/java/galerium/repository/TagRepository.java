package galerium.repository;

import galerium.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    @Query(value = "SELECT t.* FROM tags t LEFT JOIN client_tags ct ON t.id = ct.tag_id WHERE ct.user_id IS NULL", nativeQuery = true)
    List<Tag> findOrphanTags();
}