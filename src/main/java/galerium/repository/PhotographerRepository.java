package galerium.repository;

import galerium.model.Gallery;
import galerium.model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotographerRepository extends JpaRepository<Photographer, Long> {
    List<Photographer> findByGalleries_TitleContainingIgnoreCase(String title);
    Optional<Photographer> findByEmail(String email);
    List<Photographer> findByFullNameContainingIgnoreCase(String name);
}
