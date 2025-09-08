package galerium.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "galleries")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "photographer_id", nullable = false)
    private Photographer photographer;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL)
    private List<Photo> photos;

    @Schema(description = "Gallery title", example = "Vin & Elend's Wedding")
    @Column(nullable = false)
    @Size(max = 100, message = "The title cannot exceed 100 characters")
    private String title;

    @Schema(description = "Date of when the photoshoot took place.")
    @Column(nullable = false)
    private LocalDate date;

    @Schema(description = "About the gallery.")
    @Column(columnDefinition = "TEXT", nullable = true)
    private String description;

    /*
    Future features:
    private boolean downloadable;
    private boolean shared;
     */
}
