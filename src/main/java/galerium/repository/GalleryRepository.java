package galerium.repository;

import galerium.model.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    List<Gallery> findByTitleContainingIgnoreCase(String title);
    List<Gallery> findByPhotographerId(Long photographerId);
    List<Gallery> findByClientId(Long clientID);
    List<Gallery> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
