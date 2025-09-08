package galerium.repository;

import galerium.model.Gallery;
import galerium.model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotographerRepository extends JpaRepository<Photographer, Long> {
    List<Photographer> findByGalleriesContaining(Gallery gallery);
}
