package galerium.repository;

import galerium.model.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    List<Gallery> findByPhotographerId(Long photographerId);
    List<Gallery> findByClientId(Long clientID);
}
