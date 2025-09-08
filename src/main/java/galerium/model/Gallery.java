package galerium.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    private String title;

    @ManyToOne
    @JoinColumn(name = "photographer_id", nullable = false)
    private Photographer photographer;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private LocalDateTime date;
    private boolean downloadable;
    private boolean shared;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL)
    private List<Photo> photos;

// Puedes añadir campos como downloadPermission, sharePermission, etc.
}
